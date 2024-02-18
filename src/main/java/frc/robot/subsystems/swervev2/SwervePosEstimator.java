package frc.robot.subsystems.swervev2;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.swervev2.components.GenericEncodedSwerve;
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
    private static final Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0.05, 0.05, 0.3);
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
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("ROS");
        subscriber = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1});
        SmartDashboard.putData(field);
    }
    /**
     * updates odometry, should be called in periodic
     * @param gyroValueDeg current gyro value (angle of robot)
     * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
     */
    public void updatePosition(double gyroValueDeg){
        if (DriverStation.isEnabled()){
            poseEstimator.update(new Rotation2d(Math.toRadians(gyroValueDeg)),
                    new SwerveModulePosition[] {
                            frontLeftMotor.getPosition(),
                            frontRightMotor.getPosition(),
                            backLeftMotor.getPosition(),
                            backRightMotor.getPosition(),

                    });
        }
        field.setRobotPose(poseEstimator.getEstimatedPosition());
    }
    public void updatePositionWithVis(double gyroValueDeg){
        if (DriverStation.isEnabled()){
            double[] visionArray = subscriber.get();
            Pose2d visionPose = new Pose2d(visionArray[0], visionArray[1], new Rotation2d(Units.degreesToRadians(visionArray[2])));
            if (visionArray[0] != -1 && visionArray[1] != -1 && visionArray[2] != -1) {
                poseEstimator.addVisionMeasurement(visionPose, Timer.getFPGATimestamp());
            }
            updatePosition(gyroValueDeg);
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
                        frontLeftMotor.getPosition(),
                        frontRightMotor.getPosition(),
                        backLeftMotor.getPosition(),
                        backRightMotor.getPosition(),
                },new Pose2d(translation2d,new Rotation2d(radians)));
    }
    public Pose2d getEstimatedPose(){
        return field.getRobotPose();
    }

    public Field2d getField() {
        return field;
    }

}
