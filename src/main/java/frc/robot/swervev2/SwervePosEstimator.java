package frc.robot.swervev2;

import edu.wpi.first.math.Matrix;
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
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.swervev2.components.GenericEncodedSwerve;
import frc.robot.utils.Apriltag;
import frc.robot.utils.PrecisionTime;
import frc.robot.utils.RobotMode;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.math.ArrayUtils;
import frc.robot.utils.math.PoseUtils;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class to estimate the current position of the robot,
 * given the serve wheel encoders (through swerve model states)
 * and the initial position of the robot
 */
public class SwervePosEstimator {
    public record VisionMeasurements(TimestampedDoubleArray measurement, Apriltag tagId){}
    private final LinkedTransferQueue<VisionMeasurements> visionQueue = new LinkedTransferQueue<>();
    private final LinkedTransferQueue<TimestampedInteger> idQueue = new LinkedTransferQueue<>();
    private final LinkedTransferQueue<TimestampedDoubleArray> tagQueue = new LinkedTransferQueue<>();
    private final Field2d field = new Field2d();
    private final GenericEncodedSwerve frontLeftMotor;
    private final GenericEncodedSwerve frontRightMotor;
    private final GenericEncodedSwerve backLeftMotor;
    private final GenericEncodedSwerve backRightMotor;
    private final SwerveDrivePoseEstimator poseEstimator;

    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs = VecBuilder.fill(0.1, 0.1, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0.7, 0.7, 0.5);
    private static final Transform2d cameraOneTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private static final Transform2d cameraTwoTransform = new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X, Constants.CAMERA_OFFSET_FROM_CENTER_Y, new Rotation2d());
    private final TimeInterpolatableBuffer<Pose2d> poseBuffer = TimeInterpolatableBuffer.createBuffer(GameConstants.POSE_BUFFER_STORAGE_TIME);
    private final AtomicReference<Pose2d> estimatedPose;
    private final ConcurrentLinkedQueue<Double> errorTimeStampLog = new ConcurrentLinkedQueue<>();
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
        IntegerSubscriber apriltagId = table.getIntegerTopic("apriltag_id").subscribe(-1, PubSubOption.sendAll(true), PubSubOption.keepDuplicates(true));
        DoubleArraySubscriber visionMeasureSub = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1,-1}, PubSubOption.sendAll(true), PubSubOption.keepDuplicates(true));
        new Thread(
                () -> {
                    while (true){
                        TimestampedInteger[] queue = apriltagId.readQueue();
                        for (TimestampedInteger id : queue){
                            idQueue.offer(id,10, TimeUnit.MILLISECONDS);
                        }
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();
        new Thread(
                () -> {
                    while (true){
                        TimestampedDoubleArray[] queue = visionMeasureSub.readQueue();
                        for (TimestampedDoubleArray m : queue){
                            tagQueue.offer(m,10, TimeUnit.MILLISECONDS);
                        }
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();
        new Thread(
                () -> {
                    while (true){
                        try {
                            visionQueue.add(new VisionMeasurements(tagQueue.take(), Apriltag.of((int) idQueue.take().value)));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        ).start();
        new Thread(() -> {
            while (true){
                try {
                    if (Robot.getMode().equals(RobotMode.TELEOP) && Constants.ENABLE_VISION){
                        VisionMeasurements take = visionQueue.take();
                        if (!validAprilTagPose(take.measurement)){
                            return;
                        }
                        Pose2d visionPose = getVisionPose(take.measurement, take.tagId);
                        double latencyInSec = PrecisionTime.MICROSECONDS.convert(PrecisionTime.SECONDS, take.measurement.serverTime - TimeUnit.MILLISECONDS.toMicros(600));
                        if (withinThreshold(visionPose, latencyInSec)) {
                            poseEstimator.addVisionMeasurement(visionPose, latencyInSec, calcStdDev(take.tagId, visionPose));
                        }
                    }else {
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        SmartDashboard.putData(field);

    }

    private Matrix<N3, N1> calcStdDev(Apriltag tag, Pose2d visionPose) {
        double distance = tag.getPose().toTranslation2d().getDistance(visionPose.getTranslation());
        // dist vs Std
        // (0.01, 0.02)
        // (1.5, 0.05)
        // (2.1, 0.2)
        // (3, 1.5)
        double stdXY = 0.00472 * Math.exp(1.91 * distance);
        return VecBuilder.fill(stdXY,stdXY,0.5);
    }

    /**
     * updates odometry, should be called in periodic
     * @param gyroValueDeg current gyro value (angle of robot)
     * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
     */
    public void updatePosition(double gyroValueDeg){
        estimatedPose.set(poseEstimator.getEstimatedPosition());
        if (!Robot.getMode().equals(RobotMode.DISABLED)){
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

    private boolean validAprilTagPose(TimestampedDoubleArray measurement) {
        return !ArrayUtils.allMatch(measurement.value,-1.0);
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
    public void logErrors(){
        Double poll = errorTimeStampLog.poll();
        while (poll != null){
            Logger.logDouble("/robot/poseEstimator/queueSizeError", poll, Constants.ENABLE_LOGGING);
            poll = errorTimeStampLog.poll();
        }
    }

}
