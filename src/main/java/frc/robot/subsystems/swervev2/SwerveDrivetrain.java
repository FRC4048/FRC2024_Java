package frc.robot.subsystems.swervev2;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Alignable;
import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.subsystems.swervev2.components.EncodedSwerveSparkMax;
import frc.robot.subsystems.swervev2.type.GenericSwerveModule;
import frc.robot.utils.diag.DiagSparkMaxAbsEncoder;
import frc.robot.utils.diag.DiagSparkMaxEncoder;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;


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
    private double visionArrayX = 0;
    private double visionArrayY = 0;
    private double visionArrayRot = 0;



    private final DoubleArraySubscriber subscriber;
    private double visionArray[];

    Pose2d visionPose;

    private final AHRS gyro;
    private double gyroValue = 0;
    private boolean faceingTarget = false;
    private Alignable alignable = null;


    private double getGyro() {
        return (gyro.getAngle() % 360)  * -1;
    }

    @Override
    public void periodic() {
        if (Constants.SWERVE_DEBUG){
            SmartDashboard.putNumber("FL_ABS",frontLeft.getSwerveMotor().getAbsEnc().getAbsolutePosition());
            SmartDashboard.putNumber("FR_ABS",frontRight.getSwerveMotor().getAbsEnc().getAbsolutePosition());
            SmartDashboard.putNumber("BL_ABS",backLeft.getSwerveMotor().getAbsEnc().getAbsolutePosition());
            SmartDashboard.putNumber("BR_ABS",backRight.getSwerveMotor().getAbsEnc().getAbsolutePosition());
        }
       
        visionArray = subscriber.get(); 
       
        visionPose = new Pose2d(visionArray[0], visionArray[1], new Rotation2d(Units.degreesToRadians(visionArray[2])));
        gyroValue = getGyro();
        poseEstimator.updatePosition(gyroValue);
        
        if (visionArray[0] != -1 && visionArray[1] != -1 && visionArray[2] != -1) {
            if (visionArrayX != visionArray[0] && visionArrayY != visionArray[1] && visionArrayRot != visionArray[2]) {
                 poseEstimator.addVisionEstimate(visionPose, Timer.getFPGATimestamp());
            
            
            }
        
               
        }
        Logger.logPose2d("EstimatedPose",getPose(),Constants.ENABLE_LOGGING);
        SmartShuffleboard.put("Test", "x", visionArray[0]);
        SmartShuffleboard.put("Test", "Y", visionArray[1]);
        SmartShuffleboard.put("Test", "Rot", visionArray[2]);
        visionArrayX = visionArray[0];
        visionArrayY = visionArray[1];
        visionArrayRot = visionArray[2];
        


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
        this.frontRight.getSwerveMotor().getDriveMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isFrontRightInverted());
        this.frontLeft.getSwerveMotor().getDriveMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isFrontLeftInverted());
        this.backRight.getSwerveMotor().getDriveMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isBackRightInverted());
        this.backLeft.getSwerveMotor().getDriveMotor().setInverted(Constants.SWERVE_MODULE_PROFILE.isBackLeftInverted());
        this.poseEstimator = new SwervePosEstimator(encodedSwerveSparkMaxFL,encodedSwerveSparkMaxFR,encodedSwerveSparkMaxBL,encodedSwerveSparkMaxBR,kinematics,getGyro());
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

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("ROS");
        subscriber = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1});
        



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
        return new Rotation2d(Math.toRadians(gyroValue));
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
        if (Constants.SWERVE_DEBUG) {
            SmartDashboard.putString("Alignable", alignable.toString());
        }
    }
}
