package frc.robot.subsystems.swervev2;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.swervev2.components.EncodedSwerveSparkMax;
import frc.robot.subsystems.swervev2.type.GenericSwerveModule;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

/**
 * blue centric //TODO make work for red
 */
public class SwerveDrivetrain extends SubsystemBase {
    
    private final GenericSwerveModule frontLeft;
    private final GenericSwerveModule frontRight;
    private final GenericSwerveModule backLeft;
    private final GenericSwerveModule backRight;

    private final Translation2d frontLeftLocation = new Translation2d(Constants.ROBOT_LENGTH/2, Constants.ROBOT_WIDTH/2);
    private final Translation2d frontRightLocation = new Translation2d(Constants.ROBOT_LENGTH/2, -Constants.ROBOT_WIDTH/2);
    private final Translation2d backLeftLocation = new Translation2d(-Constants.ROBOT_LENGTH/2, Constants.ROBOT_WIDTH/2);
    private final Translation2d backRightLocation = new Translation2d(-Constants.ROBOT_LENGTH/2, -Constants.ROBOT_WIDTH/2);
    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(frontLeftLocation,frontRightLocation,backLeftLocation,backRightLocation);
    private final SwervePosEstimator poseEstimator;

    private final AHRS gyro;
    private double gyroValue = 0;


    private double getGyro() {
        return (gyro.getAngle() % 360) ;
    }

    @Override
    public void periodic() {
        gyroValue = getGyro();
        poseEstimator.updatePosition(gyroValue);
        SmartDashboard.putNumber("gyro",gyroValue);
        SmartDashboard.putNumber("FrontLeftAbs",frontLeft.getSwerveMotor().getAbsEnc().getAbsolutePosition());
        SmartDashboard.putNumber("FrontRightAbs",frontRight.getSwerveMotor().getAbsEnc().getAbsolutePosition());
        SmartDashboard.putNumber("BackLeftAbs",backLeft.getSwerveMotor().getAbsEnc().getAbsolutePosition());
        SmartDashboard.putNumber("BackRightAbs",backRight.getSwerveMotor().getAbsEnc().getAbsolutePosition());

    }

    public SwerveDrivetrain(SwerveIdConfig frontLeftConfig, SwerveIdConfig frontRightConfig, SwerveIdConfig backLeftConfig, SwerveIdConfig backRightConfig,
                            KinematicsConversionConfig conversionConfig, SwervePidConfig pidConfig, AHRS gyro)
    {
        this.gyro = gyro;
        EncodedSwerveSparkMax encodedSwerveSparkMaxFL = new EncodedSwerveMotorBuilder(frontLeftConfig, conversionConfig).build();
        EncodedSwerveSparkMax encodedSwerveSparkMaxFR = new EncodedSwerveMotorBuilder(frontRightConfig, conversionConfig).build();
        EncodedSwerveSparkMax encodedSwerveSparkMaxBL = new EncodedSwerveMotorBuilder(backLeftConfig, conversionConfig).build();
        EncodedSwerveSparkMax encodedSwerveSparkMaxBR = new EncodedSwerveMotorBuilder(backRightConfig, conversionConfig).build();

        this.frontLeft = new GenericSwerveModule(encodedSwerveSparkMaxFL, pidConfig.getDrivePid(),pidConfig.getSteerPid(),pidConfig.getDriveGain(),pidConfig.getSteerGain(),pidConfig.getGoalConstraint());
        this.frontRight = new GenericSwerveModule(encodedSwerveSparkMaxFR, pidConfig.getDrivePid(),pidConfig.getSteerPid(),pidConfig.getDriveGain(),pidConfig.getSteerGain(),pidConfig.getGoalConstraint());
        this.backLeft = new GenericSwerveModule(encodedSwerveSparkMaxBL, pidConfig.getDrivePid(),pidConfig.getSteerPid(),pidConfig.getDriveGain(),pidConfig.getSteerGain(),pidConfig.getGoalConstraint());
        this.backRight = new GenericSwerveModule(encodedSwerveSparkMaxBR, pidConfig.getDrivePid(),pidConfig.getSteerPid(),pidConfig.getDriveGain(),pidConfig.getSteerGain(),pidConfig.getGoalConstraint());
        this.frontRight.getSwerveMotor().getDriveMotor().setInverted(true);
        this.frontLeft.getSwerveMotor().getDriveMotor().setInverted(false);
        this.backRight.getSwerveMotor().getDriveMotor().setInverted(true);
        this.backLeft.getSwerveMotor().getDriveMotor().setInverted(false);
        this.poseEstimator = new SwervePosEstimator(encodedSwerveSparkMaxFL,encodedSwerveSparkMaxFR,encodedSwerveSparkMaxBL,encodedSwerveSparkMaxBR,kinematics,getGyro());
        this.frontLeft.getSwerveMotor().getSteerMotor().setInverted(true);
        this.frontRight.getSwerveMotor().getSteerMotor().setInverted(true);
        this.backLeft.getSwerveMotor().getSteerMotor().setInverted(true);
        this.backRight.getSwerveMotor().getSteerMotor().setInverted(true);
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

    public void resetOdometry(Translation2d pose2d) {
        poseEstimator.resetOdometry(Math.toRadians(gyroValue), pose2d);
    }
    public void resetOdometry(Pose2d pose2d) {
        resetOdometry(pose2d.getTranslation());
    }
    public void setGyroOffset(double degrees) {
        gyro.setAngleAdjustment(degrees);
    }
}
