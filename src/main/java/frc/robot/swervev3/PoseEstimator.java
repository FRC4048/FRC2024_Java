package frc.robot.swervev3;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.TimestampedDoubleArray;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Constants;
import frc.robot.swervev3.bags.OdometryMeasurementsStamped;
import frc.robot.swervev3.io.Module;
import org.littletonrobotics.junction.Logger;

public class PoseEstimator {
    private final Field2d field = new Field2d();
    private final Module frontLeft;
    private final Module frontRight;
    private final Module backLeft;
    private final Module backRight;
    private final SwerveDrivePoseEstimator poseEstimator;
    private final DoubleArraySubscriber subscriber;

    /* standard deviation of robot states, the lower the numbers arm, the more we trust odometry */
    private static final Vector<N3> stateStdDevs = VecBuilder.fill(0.01, 0.01, 0.001);

    /* standard deviation of vision readings, the lower the numbers arm, the more we trust vision */
    private static final Vector<N3> visionMeasurementStdDevs = VecBuilder.fill(0.7, 0.7, 0.3);
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
        subscriber = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1});
        SmartDashboard.putData(field);
    }
    /**
     * updates odometry, should be called in periodic
     * @param gyroValueDeg current gyro value (angle of robot)
     * @see SwerveDrivePoseEstimator#update(Rotation2d, SwerveModulePosition[])
     */
    public void updatePosition(OdometryMeasurementsStamped[] measurements){
        if (DriverStation.isEnabled()){
            for (OdometryMeasurementsStamped measurement : measurements) {
                poseEstimator.updateWithTime(
                        measurement.timestamp(),
                        Rotation2d.fromDegrees(measurement.gyroValueDeg()),
                        measurement.modulePosition()
                );
            }
        }
        field.setRobotPose(poseEstimator.getEstimatedPosition());
    }
    public void updatePositionWithVis(OdometryMeasurementsStamped[] measurements){
        if (DriverStation.isTeleop()){
            TimestampedDoubleArray visionArray = subscriber.getAtomic();

            Pose2d visionPose = new Pose2d(visionArray.value[0],
                    visionArray.value[1],
                    new Rotation2d(Units.degreesToRadians(visionArray.value[2]))
                            .rotateBy(new Rotation2d(Math.PI)))   // to match WPILIB field
                    .plus(new Transform2d(Constants.CAMERA_OFFSET_FROM_CENTER_X,Constants.CAMERA_OFFSET_FROM_CENTER_Y,new Rotation2d())); // to offset to center of bot
            if (visionArray.value[0] != -1 && visionArray.value[1] != -1 && visionArray.value[2] != -1) {
                poseEstimator.addVisionMeasurement(visionPose, Logger.getRealTimestamp());
            }
        }
        updatePosition(measurements);
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
    public Pose2d getEstimatedPose(){
        return field.getRobotPose();
    }

    public Field2d getField() {
        return field;
    }
}
