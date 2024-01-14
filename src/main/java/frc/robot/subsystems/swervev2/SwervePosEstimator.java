package frc.robot.subsystems.swervev2;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import frc.robot.subsystems.swervev2.components.GenericEncodedSwerve;

public class SwervePosEstimator {
    private final Field2d field = new Field2d();

    private final GenericEncodedSwerve frontLeftMotor;
    private final GenericEncodedSwerve frontRightMotor;
    private final GenericEncodedSwerve backLeftMotor;
    private final GenericEncodedSwerve backRightMotor;
    private final SwerveDrivePoseEstimator poseEstimator;

    public SwervePosEstimator(GenericEncodedSwerve frontLeftMotor, GenericEncodedSwerve frontRightMotor, GenericEncodedSwerve backLeftMotor, GenericEncodedSwerve backRightMotor, SwerveDriveKinematics kinematics, double initGyroValueRadians) {
        this.frontLeftMotor = frontLeftMotor;
        this.frontRightMotor = frontRightMotor;
        this.backLeftMotor = backLeftMotor;
        this.backRightMotor = backRightMotor;
        this.poseEstimator =  new SwerveDrivePoseEstimator(
                kinematics,
                new Rotation2d(initGyroValueRadians),
                new SwerveModulePosition[] {
                        frontLeftMotor.getPosition(),
                        frontRightMotor.getPosition(),
                        backLeftMotor.getPosition(),
                        backRightMotor.getPosition(),
                },
                new Pose2d());
    }
    public void updatePosition(double gyroValue){
        if (DriverStation.isEnabled()){
            poseEstimator.update(new Rotation2d(gyroValue),
                    new SwerveModulePosition[] {
                            frontLeftMotor.getPosition(),
                            frontRightMotor.getPosition(),
                            backLeftMotor.getPosition(),
                            backRightMotor.getPosition(),
                    });
        }
        field.setRobotPose(poseEstimator.getEstimatedPosition());
    }
    public void resetOdometry(double gyroValue,Pose2d pose2d){
        this.poseEstimator.resetPosition(new Rotation2d(gyroValue),
                new SwerveModulePosition[] {
                        frontLeftMotor.getPosition(),
                        frontRightMotor.getPosition(),
                        backLeftMotor.getPosition(),
                        backRightMotor.getPosition(),
                },pose2d);
    }
    public Pose2d getEstimatedPose(){
        return field.getRobotPose();
    }
}
