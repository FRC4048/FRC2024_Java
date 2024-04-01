package frc.robot.swervev2;

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
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.swervev2.components.GenericEncodedSwerve;
import frc.robot.utils.PrecisionTime;

import java.util.Optional;
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
    private final DoubleArraySubscriber subscriber;

    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs = VecBuilder.fill(0.1, 0.1, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0.7, 0.7, 0.3);
    private final TimeInterpolatableBuffer<Pose2d> poseBuffer = TimeInterpolatableBuffer.createBuffer(GameConstants.POSE_BUFFER_STORAGE_TIME);
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
        subscriber = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1}, PubSubOption.pollStorage(5), PubSubOption.sendAll(true), PubSubOption.keepDuplicates(false));
        SmartDashboard.putData(field);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            if (DriverStation.isTeleop()){
                TimestampedDoubleArray[] queue = subscriber.readQueue();
                for (TimestampedDoubleArray measurement: queue){
                    Pose2d visionPose = getVisionPose(measurement);
                    double latencyInSec = PrecisionTime.MICROSECONDS.convert(PrecisionTime.SECONDS, measurement.serverTime);
                    if (validAprilTagPose(measurement) && (withinThreshold(visionPose, latencyInSec) || distanceToTagOverride())){
                        poseEstimator.addVisionMeasurement(visionPose, latencyInSec);
                    }
                }
            }
        },0,10, TimeUnit.MILLISECONDS);
    }
    /**
     * updates odometry, should be called in periodic
     * @param gyroValueDeg current gyro value (angle of robot)
     * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
     */
    public void updatePosition(double gyroValueDeg){
        estimatedPose.set(poseEstimator.getEstimatedPosition());
        if (DriverStation.isEnabled() && Constants.ENABLE_VISION){
            Rotation2d gyroAngle = new Rotation2d(Math.toRadians(gyroValueDeg));
            SwerveModulePosition[] modulePositions = new SwerveModulePosition[] {
                    frontLeftMotor.getPosition(),
                    frontRightMotor.getPosition(),
                    backLeftMotor.getPosition(),
                    backRightMotor.getPosition(),

            };
            estimatedPose.set(poseEstimator.update(gyroAngle, modulePositions));
            synchronized (poseBuffer){
                poseBuffer.addSample(Timer.getFPGATimestamp(), estimatedPose.get());
            }
        }
        field.setRobotPose(estimatedPose.get());
    }

    //TODO this is a temporary method to account for distance from april tag.
    private boolean distanceToTagOverride() {
        return false;
    }

    private boolean withinThreshold(Pose2d visionPose, double timeStampSeconds) {
        boolean withinTime;
        Optional<Pose2d> sample;
        synchronized (poseBuffer){
            withinTime = poseBuffer.getInternalBuffer().lastEntry().getKey() - timeStampSeconds <= GameConstants.POSE_BUFFER_STORAGE_TIME;
            sample = poseBuffer.getSample(timeStampSeconds);
        }
        boolean withinDistance = sample.isPresent() && sample.get().getTranslation().getDistance(visionPose.getTranslation()) <= 1;
        return withinTime && withinDistance;
    }

    private Pose2d getVisionPose(TimestampedDoubleArray measurement){
        return new Pose2d(measurement.value[0],
                measurement.value[1],
                new Rotation2d(Units.degreesToRadians(measurement.value[2]))
                        .rotateBy(new Rotation2d(Math.PI)))   // to match WPILIB field
                .plus(new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X,Constants.CAMERA_OFFSET_FROM_CENTER_Y,new Rotation2d()));
    }

    private boolean validAprilTagPose(TimestampedDoubleArray measurement) {
        return (measurement.value[0] != -1 && measurement.value[1] != -1 && measurement.value[2] != -1);
    }

    /**
     * @param radians robot angle to reset odometry to
     * @param translation2d robot field position to reset odometry to
     * @see SwerveDrivePoseEstimator#resetPosition(Rotation2d, SwerveModulePosition[], Pose2d)
     */
    public void resetOdometry(double radians, Translation2d translation2d){
        this.poseEstimator.resetPosition(new Rotation2d(radians),
                new SwerveModulePosition[] {
                        frontLeftMotor.getPosition(),
                        frontRightMotor.getPosition(),
                        backLeftMotor.getPosition(),
                        backRightMotor.getPosition(),
                },new Pose2d(translation2d,new Rotation2d(radians)));
    }
    public Pose2d getEstimatedPose(){
        return estimatedPose.get();
    }

    public Field2d getField() {
        return field;
    }

}
