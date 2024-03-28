package frc.robot.subsystems;

import com.pathplanner.lib.path.PathPlannerPath;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.swervev2.*;
import frc.robot.swervev2.components.EncodedSwerveSparkMax;
import frc.robot.swervev2.type.GenericSwerveModule;
import frc.robot.utils.Alignable;
import frc.robot.utils.DriveMode;
import frc.robot.utils.PathPlannerUtils;
import frc.robot.utils.ThreadedGyro;
import frc.robot.utils.diag.DiagSparkMaxAbsEncoder;
import frc.robot.utils.diag.DiagSparkMaxEncoder;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;


public class SwerveDrivetrain extends SubsystemBase {

    private PathPlannerPath path;

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
    private final ThreadedGyro gyro;
    private final PIDController alignableTurnPid = new PIDController(Constants.ALIGNABLE_PID_P,Constants.ALIGNABLE_PID_I,Constants.ALIGNABLE_PID_D);
    private boolean faceingTarget = false;
    private Alignable alignable = null;
    private double frontLeftDriveCurrent;
    private double frontRightDriveCurrent;
    private double backLeftDriveCurrent;
    private double backRightDriveCurrent;
    private double frontLeftSteerCurrent;
    private double frontRightSteerCurrent;
    private double backLeftSteerCurrent;
    private double backRightSteerCurrent;
    private double totalCurrent;
    private double totalSteerCurrent;
    private double totalDriveCurrent;
    private DriveMode driveMode = DriveMode.FIELD_CENTRIC;

    @Override
    public void periodic() {
        if (Constants.DRIVE_CURRENT_DEBUG){
            frontLeftDriveCurrent = ((CANSparkMax)frontLeft.getSwerveMotor().getDriveMotor()).getOutputCurrent();
            frontRightDriveCurrent = ((CANSparkMax)frontRight.getSwerveMotor().getDriveMotor()).getOutputCurrent();
            backLeftDriveCurrent = ((CANSparkMax)backLeft.getSwerveMotor().getDriveMotor()).getOutputCurrent();
            backRightDriveCurrent = ((CANSparkMax)backRight.getSwerveMotor().getDriveMotor()).getOutputCurrent();
            frontLeftSteerCurrent = ((CANSparkMax)frontLeft.getSwerveMotor().getSteerMotor()).getOutputCurrent();
            frontRightSteerCurrent = ((CANSparkMax)frontRight.getSwerveMotor().getSteerMotor()).getOutputCurrent();
            backLeftSteerCurrent = ((CANSparkMax)backLeft.getSwerveMotor().getSteerMotor()).getOutputCurrent();
            backRightSteerCurrent = ((CANSparkMax)backRight.getSwerveMotor().getSteerMotor()).getOutputCurrent();
            totalSteerCurrent = frontLeftSteerCurrent + frontRightSteerCurrent + backRightSteerCurrent + backLeftSteerCurrent;
            totalDriveCurrent = frontLeftDriveCurrent + frontRightDriveCurrent + backLeftDriveCurrent + backRightDriveCurrent;
            totalCurrent = totalSteerCurrent + totalDriveCurrent;
            Logger.logDouble("/Robot/DriveCurrent", totalDriveCurrent, Constants.ENABLE_LOGGING);
            Logger.logDouble("/Robot/SteerCurrent", totalSteerCurrent, Constants.ENABLE_LOGGING);
            Logger.logDouble("/Robot/TotalCurrent", totalCurrent, Constants.ENABLE_LOGGING);
        }
        if (Constants.PATHPLANNER_DEBUG){
            SmartShuffleboard.putCommand("PathPlanner","Plan To Podium", PathPlannerUtils.autoFromPath(PathPlannerUtils.createManualPath(getPose(),new Pose2d(2.5,4,new Rotation2d(Math.PI)),0)));
            SmartShuffleboard.putCommand("PathPlanner","Plan To PodiumV2", PathPlannerUtils.pathToPose(new Pose2d(2.5,4,new Rotation2d(Math.PI)),0));
        }
        if (Constants.SWERVE_DEBUG){
            SmartDashboard.putNumber("FL_ABS",frontLeft.getSwerveMotor().getAbsEnc().getAbsolutePosition());
            SmartDashboard.putNumber("FR_ABS",frontRight.getSwerveMotor().getAbsEnc().getAbsolutePosition());
            SmartDashboard.putNumber("BL_ABS",backLeft.getSwerveMotor().getAbsEnc().getAbsolutePosition());
            SmartDashboard.putNumber("BR_ABS",backRight.getSwerveMotor().getAbsEnc().getAbsolutePosition());

            SmartShuffleboard.put("DriveTrain", "Total Drive Current", totalDriveCurrent);
            SmartShuffleboard.put("DriveTrain", "Total Steer Current", totalSteerCurrent);
            SmartShuffleboard.put("DriveTrain", "TOTAL Current", totalCurrent);
            SmartShuffleboard.put("GYRO", "Gyro Angle", gyro.getGyroValue());
        }
        

        if (Constants.ENABLE_VISION){
            poseEstimator.updatePositionWithVis(gyro.getGyroValue());
        }else {
            poseEstimator.updatePosition(gyro.getGyroValue());
        }
        if (Constants.SWERVE_DEBUG) {
            SmartShuffleboard.put("GYRO", "Gyro Angle", gyro.getGyroValue());
        }
    }

    public SwerveDrivetrain(SwerveIdConfig frontLeftConfig, SwerveIdConfig frontRightConfig, SwerveIdConfig backLeftConfig, SwerveIdConfig backRightConfig,
                            KinematicsConversionConfig conversionConfig, SwervePidConfig pidConfig, ThreadedGyro gyro)
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
        this.frontRight.getSwerveMotor().getDriveMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isFrontRightInverted());
        this.frontLeft.getSwerveMotor().getDriveMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isFrontLeftInverted());
        this.backRight.getSwerveMotor().getDriveMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isBackRightInverted());
        this.backLeft.getSwerveMotor().getDriveMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isBackLeftInverted());
        this.poseEstimator = new SwervePosEstimator(encodedSwerveSparkMaxFL,encodedSwerveSparkMaxFR,encodedSwerveSparkMaxBL,encodedSwerveSparkMaxBR,kinematics,gyro.getGyroValue());
        this.frontLeft.getSwerveMotor().getSteerMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isSteerInverted());
        this.frontRight.getSwerveMotor().getSteerMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isSteerInverted());
        this.backLeft.getSwerveMotor().getSteerMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isSteerInverted());
        this.backRight.getSwerveMotor().getSteerMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isSteerInverted());

        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxEncoder("DT Drive", "Front Left", Constants.DIAG_REL_SPARK_ENCODER, (CANSparkMax) frontLeft.getSwerveMotor().getDriveMotor()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxEncoder("DT Drive", "Front Right", Constants.DIAG_REL_SPARK_ENCODER, (CANSparkMax) frontRight.getSwerveMotor().getDriveMotor()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxEncoder("DT Drive", "Back Left", Constants.DIAG_REL_SPARK_ENCODER, (CANSparkMax) backLeft.getSwerveMotor().getDriveMotor()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxEncoder("DT Drive", "Back Right", Constants.DIAG_REL_SPARK_ENCODER, (CANSparkMax) backRight.getSwerveMotor().getDriveMotor()));

        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxEncoder("DT Turn", "Front Left", Constants.DIAG_REL_SPARK_ENCODER, (CANSparkMax) frontLeft.getSwerveMotor().getSteerMotor()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxEncoder("DT Turn", "Front Right", Constants.DIAG_REL_SPARK_ENCODER, (CANSparkMax) frontRight.getSwerveMotor().getSteerMotor()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxEncoder("DT Turn", "Back Left", Constants.DIAG_REL_SPARK_ENCODER, (CANSparkMax) backRight.getSwerveMotor().getSteerMotor()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxEncoder("DT Turn", "Back Right", Constants.DIAG_REL_SPARK_ENCODER, (CANSparkMax) backLeft.getSwerveMotor().getSteerMotor()));

        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxAbsEncoder("DT CanCoder", "Front Left", Constants.DIAG_ABS_SPARK_ENCODER, frontLeft.getSwerveMotor().getAbsEnc()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxAbsEncoder("DT CanCoder", "Front Right", Constants.DIAG_ABS_SPARK_ENCODER, frontRight.getSwerveMotor().getAbsEnc()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxAbsEncoder("DT CanCoder", "Back Left", Constants.DIAG_ABS_SPARK_ENCODER, backLeft.getSwerveMotor().getAbsEnc()));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxAbsEncoder("DT CanCoder", "Back Right", Constants.DIAG_ABS_SPARK_ENCODER, backRight.getSwerveMotor().getAbsEnc()));
        alignableTurnPid.enableContinuousInput(-180, 180);
    }


    public ChassisSpeeds createChassisSpeeds(double xSpeed, double ySpeed, double rotation, DriveMode driveMode) {
        return driveMode.equals(DriveMode.FIELD_CENTRIC)
                ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rotation, new Rotation2d(Math.toRadians(gyro.getGyroValue())))
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
       gyro.resetGyro();
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

    public void resetOdometry(Translation2d pose2d,Rotation2d rotation2d) {
        poseEstimator.resetOdometry(rotation2d.getRadians(), pose2d);
    }
    public void resetOdometry(Pose2d pose2d) {
        resetOdometry(pose2d.getTranslation(),pose2d.getRotation());
    }
    public void setGyroOffset(double degrees) {
        gyro.setAngleAdjustment(degrees);
    }

    public Rotation2d getGyroAngle() {
        return new Rotation2d(Math.toRadians(gyro.getGyroValue()));
    }

    /**
     * should only be set to true if you {@link #alignable} is not null
     * @param facingTarget if you are facing target or not
     */
    public void setFacingTarget(boolean facingTarget) {
        this.faceingTarget = facingTarget;
    }

    /**
     * @return return true if you have reached target, else return false
     */
    public boolean isFacingTarget() {
        return faceingTarget;
    }

    public Alignable getAlignable() {
        return alignable;
    }

    public void setAlignable(Alignable alignable) {
        this.alignable = alignable;
        if (Constants.SWERVE_DEBUG && alignable != null) {
            SmartDashboard.putString("Alignable", alignable.toString());
        }
    }

    public PIDController getAlignableTurnPid() {
        return alignableTurnPid;
    }
    public double getFrontLeftDriveCurrent() {
        return frontLeftDriveCurrent;
    }
    public double getFrontRightDriveCurrent() {
        return frontRightDriveCurrent;
    }
    public double getBackLeftDriveCurrent() {
        return backLeftDriveCurrent;
    }
    public double getBackRightDriveCurrent() {
        return backRightDriveCurrent;
    }
    public double getFrontLeftSteerCurrent() {
        return frontLeftSteerCurrent;
    }
    public double getFrontRightSteerCurrent() {
        return frontRightSteerCurrent;
    }
    public double getBackLeftSteerCurrent() {
        return backLeftSteerCurrent;
    }
    public double getBackRightSteerCurrent() {
        return backRightSteerCurrent;
    }
    public double getTotalDriveCurrent() {
        return totalDriveCurrent;
    }
    public double getTotalSteerCurrent() {
        return totalSteerCurrent;
    }
    public double getTotalCurrent() {
        return totalCurrent;
    }
    public DriveMode getDriveMode() {
        return driveMode;
    }

    public void setDriveMode(DriveMode driveMode) {
        this.driveMode = driveMode;
    }
    public ChassisSpeeds getChassisSpeeds(){
        return kinematics.toChassisSpeeds(frontLeft.getState(),frontRight.getState(),backLeft.getState(),backRight.getState());
    }
    public ChassisSpeeds getFieldChassisSpeeds() {
        return ChassisSpeeds.fromRobotRelativeSpeeds(getChassisSpeeds(),getPose().getRotation());
    }
}
