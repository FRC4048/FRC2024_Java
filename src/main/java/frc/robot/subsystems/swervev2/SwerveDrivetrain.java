package frc.robot.subsystems.swervev2;

import java.util.List;

import com.kauailabs.navx.frc.AHRS;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.subsystems.swervev2.components.EncodedSwerveSparkMax;
import frc.robot.subsystems.swervev2.type.GenericSwerveModule;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class SwerveDrivetrain extends SubsystemBase {

    private final List<Translation2d> bezierPoints;

    private final GenericSwerveModule frontLeft;
    private final GenericSwerveModule frontRight;
    private final GenericSwerveModule backLeft;
    private final GenericSwerveModule backRight;


    private final Translation2d frontLeftLocation = new Translation2d(Constants.ROBOT_LENGTH / 2, Constants.ROBOT_WIDTH / 2);
    private final Translation2d frontRightLocation = new Translation2d(Constants.ROBOT_LENGTH / 2, -Constants.ROBOT_WIDTH / 2);
    private final Translation2d backLeftLocation = new Translation2d(-Constants.ROBOT_LENGTH / 2, Constants.ROBOT_WIDTH / 2);
    private final Translation2d backRightLocation = new Translation2d(-Constants.ROBOT_LENGTH / 2, -Constants.ROBOT_WIDTH / 2);
    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
    private final SwervePosEstimator poseEstimator;

    private final PathPlannerPath path;
   


    private final AHRS gyro;
    private double gyroValue = 0;

    private double getGyro() {
        return (gyro.getAngle() % 360) * -1;
    }

    @Override
    public void periodic() {
        gyroValue = getGyro();
        poseEstimator.updatePosition(gyroValue);
        SmartDashboard.putNumber("gyro", gyroValue);

    }

    public SwerveDrivetrain(SwerveIdConfig frontLeftConfig, SwerveIdConfig frontRightConfig, SwerveIdConfig backLeftConfig, SwerveIdConfig backRightConfig, KinematicsConversionConfig conversionConfig, SwervePidConfig pidConfig, AHRS gyro) {

        this.gyro = gyro;
        EncodedSwerveSparkMax encodedSwerveSparkMaxFL = new EncodedSwerveMotorBuilder(frontLeftConfig, conversionConfig).build();
        EncodedSwerveSparkMax encodedSwerveSparkMaxFR = new EncodedSwerveMotorBuilder(frontRightConfig, conversionConfig).build();
        EncodedSwerveSparkMax encodedSwerveSparkMaxBL = new EncodedSwerveMotorBuilder(backLeftConfig, conversionConfig).build();
        EncodedSwerveSparkMax encodedSwerveSparkMaxBR = new EncodedSwerveMotorBuilder(backRightConfig, conversionConfig).build();

        this.frontLeft = new GenericSwerveModule(encodedSwerveSparkMaxFL, pidConfig.getDrivePid(), pidConfig.getSteerPid(), pidConfig.getDriveGain(), pidConfig.getSteerGain(), pidConfig.getGoalConstraint());
        this.frontRight = new GenericSwerveModule(encodedSwerveSparkMaxFR, pidConfig.getDrivePid(), pidConfig.getSteerPid(), pidConfig.getDriveGain(), pidConfig.getSteerGain(), pidConfig.getGoalConstraint());
        this.backLeft = new GenericSwerveModule(encodedSwerveSparkMaxBL, pidConfig.getDrivePid(), pidConfig.getSteerPid(), pidConfig.getDriveGain(), pidConfig.getSteerGain(), pidConfig.getGoalConstraint());
        this.backRight = new GenericSwerveModule(encodedSwerveSparkMaxBR, pidConfig.getDrivePid(), pidConfig.getSteerPid(), pidConfig.getDriveGain(), pidConfig.getSteerGain(), pidConfig.getGoalConstraint());

        this.frontRight.getSwerveMotor().getDriveMotor().setInverted(false);
        this.frontLeft.getSwerveMotor().getDriveMotor().setInverted(true);
        this.backRight.getSwerveMotor().getDriveMotor().setInverted(false);
        this.backLeft.getSwerveMotor().getDriveMotor().setInverted(true);
        poseEstimator = new SwervePosEstimator(encodedSwerveSparkMaxFL, encodedSwerveSparkMaxFR, encodedSwerveSparkMaxBL, encodedSwerveSparkMaxBR, kinematics, getGyro());
        bezierPoints = PathPlannerPath.bezierFromPoses(new Pose2d(poseEstimator.getEstimatedPose().getX()+1, poseEstimator.getEstimatedPose().getY()+1, Rotation2d.fromDegrees(0)), new Pose2d(poseEstimator.getEstimatedPose().getX()+1, poseEstimator.getEstimatedPose().getY()+1, Rotation2d.fromDegrees(0)));
        path = new PathPlannerPath(bezierPoints, new PathConstraints(3.0, 3.0, 2 * Math.PI, 4 * Math.PI),  new GoalEndState(0.0, Rotation2d.fromDegrees(0)));

    }

    public void putShuffleboardCommands() {
        SmartShuffleboard.putCommand("Test", "OnTheFly", AutoBuilder.followPath(path));
    }
    public ChassisSpeeds createChassisSpeeds(double xSpeed, double ySpeed, double rotation, boolean fieldRelative) {
        return fieldRelative
                ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rotation, new Rotation2d(Math.toRadians(gyroValue)))
                : new ChassisSpeeds(xSpeed, ySpeed, rotation);
    }

    public void drive(ChassisSpeeds speeds) {
        SwerveModuleState[] swerveModuleStates = kinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.MAX_VELOCITY);
        setModuleStates(swerveModuleStates);
    }

    public ChassisSpeeds speedsFromStates() {
        return kinematics.toChassisSpeeds(frontLeft.getState(), frontRight.getState(), backLeft.getState(), backRight.getState());
    }

    private void setModuleStates(SwerveModuleState[] desiredStates) {
        frontLeft.setDesiredState(desiredStates[0]);
        frontRight.setDesiredState(desiredStates[1]);
        backLeft.setDesiredState(desiredStates[2]);
        backRight.setDesiredState(desiredStates[3]);
    }

    public void stopMotor() {
        frontLeft.getSwerveMotor().getSteerMotor().set(0.0);
        frontLeft.getSwerveMotor().getDriveMotor().set(0.0);
        frontRight.getSwerveMotor().getSteerMotor().set(0.0);
        frontRight.getSwerveMotor().getDriveMotor().set(0.0);
        backLeft.getSwerveMotor().getSteerMotor().set(0.0);
        backLeft.getSwerveMotor().getDriveMotor().set(0.0);
        backRight.getSwerveMotor().getSteerMotor().set(0.0);
        backRight.getSwerveMotor().getDriveMotor().set(0.0);
    }

    public void zeroRelativeEncoders() {
        frontLeft.getSwerveMotor().resetRelEnc();
        frontRight.getSwerveMotor().resetRelEnc();
        backLeft.getSwerveMotor().resetRelEnc();
        backRight.getSwerveMotor().resetRelEnc();
    }

    public void setSteerOffset(double absEncoderZeroFL, double absEncoderZeroFR, double absEncoderZeroBL, double absEncoderZeroBR) {
        frontLeft.getSwerveMotor().setSteerOffset(absEncoderZeroFL);
        frontRight.getSwerveMotor().setSteerOffset(absEncoderZeroFR);
        backLeft.getSwerveMotor().setSteerOffset(absEncoderZeroBL);
        backRight.getSwerveMotor().setSteerOffset(absEncoderZeroBR);
    }


 

    public void resetGyro() {
        gyro.reset();
    }

    public GenericSwerveModule getFrontLeft() {
        return frontLeft;
    }

    public GenericSwerveModule getFrontRight() {
        return frontRight;
    }

    public GenericSwerveModule getBackLeft() {
        return backLeft;
    }

    public GenericSwerveModule getBackRight() {
        return backRight;
    }

    public Pose2d getPose() {
        return poseEstimator.getEstimatedPose();
    }

    public SwervePosEstimator getPoseEstimator() {
        return poseEstimator;
    }

    public void resetOdometry(Pose2d pose2d) {
        poseEstimator.resetOdometry(gyroValue, pose2d);

    }
}
