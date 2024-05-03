package frc.robot.swervev3;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.swervev3.bags.OdometryMeasurementsStamped;
import frc.robot.swervev3.io.Module;
import frc.robot.utils.Apriltag;
import frc.robot.utils.PrecisionTime;
import frc.robot.utils.RobotMode;
import frc.robot.utils.math.ArrayUtils;
import frc.robot.utils.math.PoseUtils;
import org.littletonrobotics.junction.Logger;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class PoseEstimator {
    public record VisionMeasurement(TimestampedDoubleArray measurement, Apriltag tag, double timeOfMeasurement){}
    private final Field2d field = new Field2d();
    private final Module frontLeft;
    private final Module frontRight;
    private final Module backLeft;
    private final Module backRight;
    private final SwerveDrivePoseEstimator poseEstimator;
    private final DoubleArraySubscriber visionMeasurementSubscriber;
    private final IntegerArraySubscriber apriltagIdSubscriber;

    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs = VecBuilder.fill(0.15, 0.15, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0.4, 0.4, 0.5);
    private static final Transform2d cameraOneTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private static final Transform2d cameraTwoTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private final TimeInterpolatableBuffer<Pose2d> robotPoses = TimeInterpolatableBuffer.createBuffer(Constants.POSE_BUFFER_STORAGE_TIME);
    private final Queue<VisionMeasurement> visionPoses = new LinkedList<>();
    public PoseEstimator(Module frontLeftMotor, Module frontRightMotor, Module backLeftMotor, Module backRightMotor, SwerveDriveKinematics kinematics, double initGyroValueDeg) {
        this.frontLeft = frontLeftMotor;
        this.frontRight = frontRightMotor;
        this.backLeft = backLeftMotor;
        this.backRight = backRightMotor;
        this.poseEstimator =  new SwerveDrivePoseEstimator(
                kinematics,
                new Rotation2d(Math.toRadians(initGyroValueDeg)),
                new SwerveModulePosition[] {
                        frontLeft.getPositions()[0].modulePosition(),
                        frontRight.getPositions()[0].modulePosition(),
                        backLeft.getPositions()[0].modulePosition(),
                        backRight.getPositions()[0].modulePosition(),
                },
                new Pose2d(),
                stateStdDevs,
                visionMeasurementStdDevs);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("ROS");
        visionMeasurementSubscriber = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1,-1}, PubSubOption.pollStorage(10), PubSubOption.sendAll(true));
        apriltagIdSubscriber = table.getIntegerArrayTopic("apriltag_id").subscribe(new long[]{-1,-1});
        SmartDashboard.putData(field);
    }
    /**
     * updates odometry, should be called in periodic
     * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
     */
    public void updatePosition(OdometryMeasurementsStamped[] measurements){
        if (DriverStation.isEnabled()){
            for (OdometryMeasurementsStamped measurement : measurements) {
                Pose2d pose2d = poseEstimator.updateWithTime(
                        measurement.timestamp(),
                        Rotation2d.fromDegrees(measurement.gyroValueDeg()),
                        measurement.modulePosition()
                );
                robotPoses.addSample(measurement.timestamp(), pose2d);
            }
        }
        field.setRobotPose(poseEstimator.getEstimatedPosition());
    }
    private boolean validAprilTagPose(TimestampedDoubleArray measurement) {
        return !ArrayUtils.allMatch(measurement.value,-1.0);
    }
    public void updateVision(){
        if (Robot.getMode().equals(RobotMode.TELEOP) && Constants.ENABLE_VISION){
            long[] tagData = apriltagIdSubscriber.get();
            TimestampedDoubleArray[] queue = visionMeasurementSubscriber.readQueue();
            for (TimestampedDoubleArray measurement : queue){
                if (validAprilTagPose(measurement)){
                    double latencyInSec = PrecisionTime.MICROSECONDS.convert(PrecisionTime.SECONDS, measurement.serverTime - TimeUnit.MILLISECONDS.toMicros((long) measurement.value[3]));
                    visionPoses.add(new VisionMeasurement(measurement, Apriltag.of((int) tagData[0]), latencyInSec));
                }
            }
            if (Constants.FILTER_VISION_POSES){
                while (visionPoses.size() >= 2){
                    VisionMeasurement m1 = visionPoses.poll();
                    VisionMeasurement m2 = visionPoses.poll();
                    Optional<Pose2d> odomPoseAtVis1;
                    Optional<Pose2d> odomPoseAtVis2;
                    Pose2d vision1Pose;
                    Pose2d vision2Pose;
                    if (m1 == null || m2 == null){
                        continue;
                    }
                    odomPoseAtVis1 = robotPoses.getSample(m1.timeOfMeasurement());
                    odomPoseAtVis2 = robotPoses.getSample(m2.timeOfMeasurement());
                    if (odomPoseAtVis1.isEmpty() || odomPoseAtVis2.isEmpty()){
                        continue;
                    }
                    vision1Pose = getVisionPose(m1.measurement(), m1.tag());
                    vision2Pose = getVisionPose(m2.measurement(), m2.tag());

                    double odomDiff1To2 = odomPoseAtVis1.get().getTranslation().getDistance(odomPoseAtVis2.get().getTranslation());
                    double visionDiff1To2 = vision1Pose.getTranslation().getDistance(vision2Pose.getTranslation());
                    double diff = Math.abs(odomDiff1To2 - visionDiff1To2);
                    Logger.recordOutput("odometryVisionTheta", diff);
                    if (Math.abs(diff) <= Constants.VISION_CONSISTANCY_THRESHOLD){
                        poseEstimator.addVisionMeasurement(vision1Pose, m1.timeOfMeasurement());
                        poseEstimator.addVisionMeasurement(vision2Pose, m2.timeOfMeasurement());
                    }
                }
            }else{
                VisionMeasurement measurement = visionPoses.poll();
                while (measurement != null){
                    poseEstimator.addVisionMeasurement(getVisionPose(measurement.measurement,measurement.tag), measurement.timeOfMeasurement);
                    measurement = visionPoses.poll();
                }
            }

        }
    }

    /**
     * @param radians robot angle to reset odometry to
     * @param translation2d robot field position to reset odometry to
     * @see SwerveDrivePoseEstimator#resetPosition(Rotation2d, SwerveModulePosition[], Pose2d)
     */
    public void resetOdometry(double radians, Translation2d translation2d){
        this.poseEstimator.resetPosition(new Rotation2d(radians),
                new SwerveModulePosition[] {
                        frontLeft.getPositions()[0].modulePosition(),
                        frontRight.getPositions()[0].modulePosition(),
                        backLeft.getPositions()[0].modulePosition(),
                        backRight.getPositions()[0].modulePosition(),
                },new Pose2d(translation2d,new Rotation2d(radians)));
    }
    private Pose2d getVisionPose(TimestampedDoubleArray measurement, Apriltag tag){
        Translation2d visionPose = new Translation2d(measurement.value[0], measurement.value[1]);
        double slope = PoseUtils.slope(tag.getPose().toTranslation2d(),new Translation2d(visionPose.getX(), visionPose.getY()));
        Rotation2d facingAngle = new Rotation2d(Math.atan(slope));
        Transform2d camTransform;
        double visionCentricAngle = (getEstimatedPose().getRotation().getDegrees() + Math.PI) % 360;
        //this would be used if we had another camera that was mounted 180 degrees from current camera
        if (Math.abs(facingAngle.getDegrees() - visionCentricAngle) < 90){
            camTransform = cameraOneTransform;
        } else {
            camTransform = cameraTwoTransform;
        }
        return new Pose2d(measurement.value[0],
                measurement.value[1],
                getEstimatedPose().getRotation())
                .plus(camTransform);
    }

    public Pose2d getEstimatedPose(){
        return poseEstimator.getEstimatedPosition();
    }

    public Field2d getField() {
        return field;
    }
}
