package frc.robot.subsystems.swervev3;

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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LoggableSystem;
import frc.robot.subsystems.apriltags.ApriltagIO;
import frc.robot.subsystems.apriltags.ApriltagInputs;
import frc.robot.subsystems.swervev3.bags.OdometryMeasurement;
import frc.robot.subsystems.swervev3.bags.VisionMeasurement;
import frc.robot.subsystems.swervev3.io.Module;
import frc.robot.utils.RobotMode;
import frc.robot.utils.advanced.Apriltag;
import frc.robot.utils.math.ArrayUtils;
import frc.robot.utils.math.PoseUtils;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class PoseEstimator {
    private final Field2d field = new Field2d();
    private final Module frontLeft;
    private final Module frontRight;
    private final Module backLeft;
    private final Module backRight;
    private final LoggableSystem<ApriltagIO, ApriltagInputs> apriltagSystem;
    private final SwerveDrivePoseEstimator poseEstimator1;
    private final SwerveDrivePoseEstimator poseEstimator2;

    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs1 = VecBuilder.fill(0.05, 0.05, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs1 = VecBuilder.fill(0.5, 0.5, 0.5);
    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs2 = VecBuilder.fill(0.01, 0.01, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs2 = VecBuilder.fill(0.2, 0.2, 0.5);
    private static final Transform2d cameraOneTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private static final Transform2d cameraTwoTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private final TimeInterpolatableBuffer<Pose2d> robotPoses1 = TimeInterpolatableBuffer.createBuffer(Constants.POSE_BUFFER_STORAGE_TIME);
    private final TimeInterpolatableBuffer<Pose2d> robotPoses2 = TimeInterpolatableBuffer.createBuffer(Constants.POSE_BUFFER_STORAGE_TIME);
    private final Queue<VisionMeasurement> visionPoses = new LinkedList<>();

    public PoseEstimator(Module frontLeftMotor, Module frontRightMotor, Module backLeftMotor, Module backRightMotor, ApriltagIO apriltagIO, SwerveDriveKinematics kinematics, double initGyroValueDeg) {
        this.frontLeft = frontLeftMotor;
        this.frontRight = frontRightMotor;
        this.backLeft = backLeftMotor;
        this.backRight = backRightMotor;
        this.apriltagSystem = new LoggableSystem<>(apriltagIO, new ApriltagInputs());
        this.poseEstimator1 = new SwerveDrivePoseEstimator(
                kinematics,
                new Rotation2d(Math.toRadians(initGyroValueDeg)),
                new SwerveModulePosition[]{
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition(),
                },
                new Pose2d(),
                stateStdDevs1,
                visionMeasurementStdDevs1);
        poseEstimator2 = new SwerveDrivePoseEstimator(kinematics,
                new Rotation2d(Math.toRadians(initGyroValueDeg)),
                new SwerveModulePosition[]{
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition(),
                },
                new Pose2d(),
                stateStdDevs2,
                visionMeasurementStdDevs2);

        SmartDashboard.putData(field);
    }

    public void updateInputs() {
        apriltagSystem.updateInputs();
    }

    /**
     * updates odometry, should be called in periodic
     *
     * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
     */
    public void updatePosition(OdometryMeasurement m) {
        if (DriverStation.isEnabled()) {
            Pose2d pose2d1 = poseEstimator1.update(Rotation2d.fromDegrees(m.gyroValueDeg()), m.modulePosition());
            Pose2d pose2d2 = poseEstimator2.update(Rotation2d.fromDegrees(m.gyroValueDeg()), m.modulePosition());
            robotPoses1.addSample(Logger.getRealTimestamp(), pose2d1);
            robotPoses2.addSample(Logger.getRealTimestamp(), pose2d2);
        }
        field.setRobotPose(poseEstimator1.getEstimatedPosition());
    }

    private boolean validAprilTagPose(double[] measurement) {
        return !ArrayUtils.allMatch(measurement, -1.0) && measurement.length == 3;
    }

    public void updateVision() {
        if (Robot.getMode().equals(RobotMode.TELEOP) && Constants.ENABLE_VISION) {
            for (int i = 0; i < apriltagSystem.getInputs().timestamp.length; i++) {
                double[] pos = new double[]{
                        apriltagSystem.getInputs().posX[i], apriltagSystem.getInputs().posY[i],
                        apriltagSystem.getInputs().rotationDeg[i]
                };
                if (validAprilTagPose(pos)) {
                    double diff = apriltagSystem.getInputs().serverTime[i] - apriltagSystem.getInputs().timestamp[i];
                    double latencyInSec = diff / 1000;
                    visionPoses.add(new VisionMeasurement(new Pose2d(pos[0], pos[1], getEstimatedPose1().getRotation()), Apriltag.of(apriltagSystem.getInputs().apriltagNumber[i]), latencyInSec));
                }
            }
            if (Constants.FILTER_VISION_POSES1 || Constants.FILTER_VISION_POSES2) {
                boolean usingFuturePose = false;
                double[] visionOdomDiffs1 = new double[visionPoses.size()];
                double[] visionOdomDiffs2 = new double[visionPoses.size()];
                int index = 0;
                while (visionPoses.size() >= 2) {
                    index++;
                    VisionMeasurement m1 = visionPoses.poll();
                    VisionMeasurement m2 = visionPoses.poll();
                    if (m1 == null || m2 == null) {
                        continue;
                    }
                    Pose2d vision1Pose = getVisionPose(m1.measurement(), m1.tag());
                    Pose2d vision2Pose = getVisionPose(m2.measurement(), m2.tag());
                    boolean valid1 = filterVision(m1, m2, vision1Pose, vision2Pose, robotPoses1, visionOdomDiffs1, index);
                    boolean valid2 = filterVision(m1, m2, vision1Pose, vision2Pose, robotPoses1, visionOdomDiffs2, index);
                    if (valid1 || !Constants.FILTER_VISION_POSES1) {
                        poseEstimator1.addVisionMeasurement(vision1Pose, m1.timeOfMeasurement());
                        poseEstimator1.addVisionMeasurement(vision2Pose, m2.timeOfMeasurement());
                    }
                    if (valid2 || !Constants.FILTER_VISION_POSES2){
                        poseEstimator2.addVisionMeasurement(vision1Pose, m1.timeOfMeasurement());
                        poseEstimator2.addVisionMeasurement(vision2Pose, m2.timeOfMeasurement());
                    }
                }
                Logger.recordOutput("usingFuturePoseEstimation", usingFuturePose);
                Logger.recordOutput("visionOdomDiffs1", visionOdomDiffs1);
                Logger.recordOutput("visionOdomDiffs2", visionOdomDiffs2);
            } else {
                VisionMeasurement measurement = visionPoses.poll();
                while (measurement != null) {
                    Pose2d pose = getVisionPose(measurement.measurement(), measurement.tag());
                    poseEstimator1.addVisionMeasurement(pose, measurement.timeOfMeasurement());
                    poseEstimator2.addVisionMeasurement(pose, measurement.timeOfMeasurement());
                    measurement = visionPoses.poll();
                }
            }

        }
    }

    private static boolean filterVision(VisionMeasurement m1, VisionMeasurement m2, Pose2d v1Pose, Pose2d v2Pose, TimeInterpolatableBuffer<Pose2d> buffer, double[] diffs, int diffIndex) {
        Double lastEntry = buffer.getInternalBuffer().lastEntry().getKey();
        if (lastEntry != null) {
            if (lastEntry < m1.timeOfMeasurement() || lastEntry < m2.timeOfMeasurement()) {
                return false;
            }
        }
        Optional<Pose2d> odomPoseAtVis1 = buffer.getSample(m1.timeOfMeasurement());
        Optional<Pose2d> odomPoseAtVis2 = buffer.getSample(m2.timeOfMeasurement());
        if (odomPoseAtVis1.isEmpty() || odomPoseAtVis2.isEmpty()) {
            return false;
        }
        double odomDiff1To2 = odomPoseAtVis1.get().getTranslation().getDistance(odomPoseAtVis2.get().getTranslation());
        double visionDiff1To2 = v1Pose.getTranslation().getDistance(v2Pose.getTranslation());
        double diff = Math.abs(odomDiff1To2 - visionDiff1To2);
        diffs[diffIndex] = diff;
        return Math.abs(diff) <= Constants.VISION_CONSISTANCY_THRESHOLD;
    }

    /**
     * @param radians       robot angle to reset odometry to
     * @param translation2d robot field position to reset odometry to
     * @see SwerveDrivePoseEstimator#resetPosition(Rotation2d, SwerveModulePosition[], Pose2d)
     */
    public void resetOdometry(double radians, Translation2d translation2d) {
        this.poseEstimator1.resetPosition(new Rotation2d(radians),
                new SwerveModulePosition[]{
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition(),
                }, new Pose2d(translation2d, new Rotation2d(radians)));
        this.poseEstimator2.resetPosition(new Rotation2d(radians),
                new SwerveModulePosition[]{
                        frontLeft.getPosition(),
                        frontRight.getPosition(),
                        backLeft.getPosition(),
                        backRight.getPosition(),
                }, new Pose2d(translation2d, new Rotation2d(radians)));
        field.setRobotPose(poseEstimator1.getEstimatedPosition());
    }

    private Pose2d getVisionPose(Pose2d measurement, Apriltag tag) {
        double slope = PoseUtils.slope(tag.getPose().toTranslation2d(), new Translation2d(measurement.getX(), measurement.getY()));
        Rotation2d facingAngle = new Rotation2d(Math.atan(slope));
        Transform2d camTransform;
        double visionCentricAngle = (getEstimatedPose1().getRotation().getDegrees() + Math.PI) % 360;
        //this would be used if we had another camera that was mounted 180 degrees from current camera
        if (Math.abs(facingAngle.getDegrees() - visionCentricAngle) < 90) {
            camTransform = cameraOneTransform;
        } else {
            camTransform = cameraTwoTransform;
        }
        return measurement.plus(camTransform);
    }

    @AutoLogOutput
    public Pose2d getEstimatedPose1() {
        return poseEstimator1.getEstimatedPosition();
    }
    @AutoLogOutput
    public Pose2d getEstimatedPose2() {
        return poseEstimator2.getEstimatedPosition();
    }

    public Field2d getField() {
        return field;
    }
}
