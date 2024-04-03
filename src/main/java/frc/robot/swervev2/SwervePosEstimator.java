package frc.robot.swervev2;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.swervev2.components.GenericEncodedSwerve;
import frc.robot.utils.Apriltag;
import frc.robot.utils.PrecisionTime;
import frc.robot.utils.RobotMode;
import frc.robot.utils.math.ArrayUtils;
import frc.robot.utils.math.PoseUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class to estimate the current position of the robot,
 * given the serve wheel encoders (through swerve model states)
 * and the initial position of the robot
 */
public class SwervePosEstimator{
    private final Field2d field = new Field2d();
    private final GenericEncodedSwerve frontLeftMotor;
    private final GenericEncodedSwerve frontRightMotor;
    private final GenericEncodedSwerve backLeftMotor;
    private final GenericEncodedSwerve backRightMotor;
    private final SwerveDrivePoseEstimator poseEstimator;
    private final DoubleArraySubscriber visionMeasurementSubscriber;
    private final IntegerArraySubscriber apriltagIdSubscriber;

    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs = VecBuilder.fill(0.1, 0.1, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0.7, 0.7, 0.5);
    private static final Transform2d cameraOneTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private static final Transform2d cameraTwoTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private final AtomicReference<Pose2d> estimatedPose;
    public SwervePosEstimator(GenericEncodedSwerve frontLeftMotor, GenericEncodedSwerve frontRightMotor, GenericEncodedSwerve backLeftMotor, GenericEncodedSwerve backRightMotor, SwerveDriveKinematics kinematics, double initGyroValueDeg) {
        this.frontLeftMotor = frontLeftMotor;
        this.frontRightMotor = frontRightMotor;
        this.backLeftMotor = backLeftMotor;
        this.backRightMotor = backRightMotor;
        this.poseEstimator =  new SwerveDrivePoseEstimator(
                kinematics,
                new Rotation2d(Math.toRadians(initGyroValueDeg)),
                new SwerveModulePosition[] {
                        frontLeftMotor.getPosition(),
                        frontRightMotor.getPosition(),
                        backLeftMotor.getPosition(),
                        backRightMotor.getPosition(),
                },
                new Pose2d(),
                stateStdDevs,
                visionMeasurementStdDevs);
        estimatedPose = new AtomicReference<>(poseEstimator.getEstimatedPosition());
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("ROS");
        visionMeasurementSubscriber = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1,-1}, PubSubOption.pollStorage(10), PubSubOption.sendAll(true));
        apriltagIdSubscriber = table.getIntegerArrayTopic("apriltag_id").subscribe(new long[]{-1,-1});
        SmartDashboard.putData(field);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            if (Robot.getMode().equals(RobotMode.TELEOP) && Constants.ENABLE_VISION){
                TimestampedDoubleArray[] queue = visionMeasurementSubscriber.readQueue();
                long[] tagData = apriltagIdSubscriber.get();
                SmartDashboard.putNumber("SIZE", tagData.length);
                for (TimestampedDoubleArray measurement : queue) {
                    if (!validAprilTagPose(measurement)) {
                        SmartDashboard.putBoolean("INVALID",true);
                        return;
                    }
                    SmartDashboard.putBoolean("INVALID",false);
                    Pose2d visionPose;
                    visionPose = getVisionPose(measurement, Apriltag.of((int) tagData[0]));
                    double latencyInSec = PrecisionTime.MICROSECONDS.convert(PrecisionTime.SECONDS, measurement.serverTime - TimeUnit.MILLISECONDS.toMicros((long) measurement.value[3]));
                    synchronized (poseEstimator){
                        poseEstimator.addVisionMeasurement(visionPose, latencyInSec, calcStdDev(Apriltag.of((int) tagData[0]), visionPose));
                    }
                }
            }
        },0,20, TimeUnit.MILLISECONDS);
    }

    //TODO make robot pose
    private Matrix<N3, N1> calcStdDev(Apriltag tag, Pose2d visionPose) {
        double distance = new Translation2d(tag.getX(),tag.getY()).getDistance(visionPose.getTranslation());
        // dist vs Std
        // (0.01, 0.02)
        // (1.5, 0.05)
        // (2.1, 0.2)
        // (3, 1.5)
        SmartDashboard.putNumber("DIST", distance);
        double stdXY = 0.00472 * Math.exp(1.91 * distance);
        Vector<N3> fill = VecBuilder.fill(stdXY, stdXY, 0.5);
        SmartDashboard.putNumber("VARIANCE", stdXY);
        return fill;
    }

    /**
     * updates odometry, should be called in periodic
     * @param gyroValueDeg current gyro value (angle of robot)
     * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
     */
    public void updatePosition(double gyroValueDeg){
        synchronized (poseEstimator){
            estimatedPose.set(poseEstimator.getEstimatedPosition());
        }
        if (!Robot.getMode().equals(RobotMode.DISABLED)){
            Rotation2d gyroAngle = new Rotation2d(Math.toRadians(gyroValueDeg));
            SwerveModulePosition[] modulePositions = new SwerveModulePosition[] {
                    frontLeftMotor.getPosition(),
                    frontRightMotor.getPosition(),
                    backLeftMotor.getPosition(),
                    backRightMotor.getPosition(),

            };
            synchronized (poseEstimator){
                estimatedPose.set(poseEstimator.update(gyroAngle, modulePositions));
            }
        }
        field.setRobotPose(estimatedPose.get());
    }

    private Pose2d getVisionPose(TimestampedDoubleArray measurement, Apriltag tag){
        Translation2d visionPose = new Translation2d(measurement.value[0], measurement.value[1]);
        Pose2d estPose = estimatedPose.get();
        double slope = PoseUtils.slope(tag.getPose().toTranslation2d(),new Translation2d(visionPose.getX(), visionPose.getY()));
        Rotation2d facingAngle = new Rotation2d(Math.atan(slope));
        Transform2d camTransform;
        double visionCentricAngle = (estPose.getRotation().getDegrees() + Math.PI) % 360;
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
    private Pose2d getVisionPose(TimestampedDoubleArray measurement){
        return getVisionPose(measurement, Apriltag.of((int) measurement.value[3]));
    }

    private boolean validAprilTagPose(TimestampedDoubleArray measurement) {
        return !ArrayUtils.allMatch(measurement.value,-1.0);
    }

    /**
     * @param radians robot angle to reset odometry to
     * @param translation2d robot field position to reset odometry to
     * @see SwerveDrivePoseEstimator#resetPosition(Rotation2d, SwerveModulePosition[], Pose2d)
     */
    public void resetOdometry(double radians, Translation2d translation2d){
        synchronized (poseEstimator){
            this.poseEstimator.resetPosition(new Rotation2d(radians),
                    new SwerveModulePosition[] {
                            frontLeftMotor.getPosition(),
                            frontRightMotor.getPosition(),
                            backLeftMotor.getPosition(),
                            backRightMotor.getPosition(),
                    },new Pose2d(translation2d,new Rotation2d(radians)));
        }
    }
    public Pose2d getEstimatedPose(){
        return estimatedPose.get();
    }

    public Field2d getField() {
        return field;
    }

}
