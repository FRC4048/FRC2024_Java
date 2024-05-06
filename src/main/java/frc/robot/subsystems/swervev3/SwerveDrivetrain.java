package frc.robot.subsystems.swervev3;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.subsystems.gyro.GyroIO;
import frc.robot.subsystems.gyro.GyroInputs;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.subsystems.swervev3.bags.ModulePositionStamped;
import frc.robot.subsystems.swervev3.bags.OdometryMeasurementsStamped;
import frc.robot.subsystems.swervev3.io.Module;
import frc.robot.subsystems.swervev3.io.ModuleIO;
import frc.robot.subsystems.apriltags.ApriltagIO;
import frc.robot.utils.Alignable;
import frc.robot.utils.DriveMode;
import org.littletonrobotics.junction.Logger;

import java.util.Arrays;

public class SwerveDrivetrain extends SubsystemBase {
    private final Module frontLeft;
    private final Module frontRight;
    private final Module backLeft;
    private final Module backRight;

    private final Translation2d frontLeftLocation = new Translation2d(Constants.ROBOT_LENGTH/2, Constants.ROBOT_WIDTH/2);
    private final Translation2d frontRightLocation = new Translation2d(Constants.ROBOT_LENGTH/2, -Constants.ROBOT_WIDTH/2);
    private final Translation2d backLeftLocation = new Translation2d(-Constants.ROBOT_LENGTH/2, Constants.ROBOT_WIDTH/2);
    private final Translation2d backRightLocation = new Translation2d(-Constants.ROBOT_LENGTH/2, -Constants.ROBOT_WIDTH/2);
    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(frontLeftLocation,frontRightLocation,backLeftLocation,backRightLocation);
//    private final SwervePosEstimator poseEstimator;
    private final GyroIO gyroIO;
    private final GyroInputs gyroInputs = new GyroInputs();
    private final PIDController alignableTurnPid = new PIDController(Constants.ALIGNABLE_PID_P,Constants.ALIGNABLE_PID_I,Constants.ALIGNABLE_PID_D);
    private boolean faceingTarget = false;
    private Alignable alignable = null;
    private DriveMode driveMode = DriveMode.FIELD_CENTRIC;
    private final PoseEstimator poseEstimator;


    public SwerveDrivetrain(ModuleIO frontLeftIO, ModuleIO frontRightIO, ModuleIO backLeftIO, ModuleIO backRightIO, GyroIO gyroIO, ApriltagIO apriltagIO, SwervePidConfig pidConfig) {
        this.frontLeft = new Module(frontLeftIO, pidConfig, "frontLeft");
        this.frontRight = new Module(frontRightIO, pidConfig, "frontRight");
        this.backLeft = new Module(backLeftIO, pidConfig, "backLeft");
        this.backRight = new Module(backRightIO, pidConfig, "backRight");
        this.gyroIO = gyroIO;
        alignableTurnPid.enableContinuousInput(-180,180);
        OdometryThread.getInstance().start();
        this.poseEstimator = new PoseEstimator(frontLeft, frontRight, backLeft, backRight, apriltagIO, kinematics, getLastGyro());
    }

    @Override
    public void periodic() {
        processInputs();
        Logger.recordOutput("OdometryLockHoldEstimate", OdometryThread.getInstance().getLock().getQueueLength());
        poseEstimator.updateInputs();
        ModulePositionStamped[] flPos = frontLeft.getPositions();
        ModulePositionStamped[] frPos = frontLeft.getPositions();
        ModulePositionStamped[] blPos = frontLeft.getPositions();
        ModulePositionStamped[] brPos = frontLeft.getPositions();
        OdometryMeasurementsStamped[] entries = new OdometryMeasurementsStamped[flPos.length];
        boolean inSync = true;
        for (int i = 0; i < flPos.length; i++) {
            entries[i] = new OdometryMeasurementsStamped(
                    new SwerveModulePosition[]{
                            flPos[i].modulePosition(),
                            frPos[i].modulePosition(),
                            blPos[i].modulePosition(),
                            brPos[i].modulePosition()
                    },
                    gyroInputs.anglesInDeg[i], flPos[i].timestamp()
            );
            if (Arrays.stream(new double[]{flPos[i].timestamp(), frPos[i].timestamp(), blPos[i].timestamp(), brPos[i].timestamp(), gyroInputs.anglesTimeStamps[i]}).distinct().count() != 1){
                inSync = false;
            }
        }
        poseEstimator.updatePosition(entries);
        poseEstimator.updateVision();
        Logger.recordOutput("OdomInSync", inSync);
    }

    private void processInputs() {
        frontLeft.updateInputs();
        frontRight.updateInputs();
        backLeft.updateInputs();
        backRight.updateInputs();
        frontLeft.processInputs();
        frontRight.processInputs();
        backLeft.processInputs();
        backRight.processInputs();
        gyroIO.updateInputs(gyroInputs);
        Logger.processInputs("GyroInputs", gyroInputs);
    }

    public ChassisSpeeds createChassisSpeeds(double xSpeed, double ySpeed, double rotation, DriveMode driveMode) {
        return driveMode.equals(DriveMode.FIELD_CENTRIC)
                ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rotation, Rotation2d.fromDegrees(getLastGyro()))
                : new ChassisSpeeds(xSpeed, ySpeed, rotation);
    }

    public void drive(ChassisSpeeds speeds) {
        SwerveModuleState[] swerveModuleStates = kinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.MAX_VELOCITY);
        setModuleStates(swerveModuleStates);
    }


    public ChassisSpeeds speedsFromStates() {
        return kinematics.toChassisSpeeds(frontLeft.getLatestState(), frontRight.getLatestState(), backLeft.getLatestState(), backRight.getLatestState());
    }

    private void setModuleStates(SwerveModuleState[] desiredStates) {
        frontLeft.setState(desiredStates[0]);
        frontRight.setState(desiredStates[1]);
        backLeft.setState(desiredStates[2]);
        backRight.setState(desiredStates[3]);
    }

    public void stopMotor() {
        frontLeft.stop();
        frontRight.stop();
        backLeft.stop();
        backRight.stop();
    }

    public void zeroRelativeEncoders() {
        frontLeft.resetRelativeEnc();
        frontRight.resetRelativeEnc();
        backLeft.resetRelativeEnc();
        backRight.resetRelativeEnc();
    }

    public void setSteerOffset(double absEncoderZeroFL, double absEncoderZeroFR, double absEncoderZeroBL, double absEncoderZeroBR) {
        frontLeft.setSteerOffset(absEncoderZeroFL);
        frontRight.setSteerOffset(absEncoderZeroFR);
        backLeft.setSteerOffset(absEncoderZeroBL);
        backRight.setSteerOffset(absEncoderZeroBR);
    }

    public void resetGyro() {
        gyroIO.resetGyro();
    }
    public double getLastGyro(){
        return gyroInputs.anglesInDeg[gyroInputs.anglesInDeg.length - 1];
    }
    public void setDriveMode(DriveMode driveMode) {
        this.driveMode = driveMode;
    }
    public DriveMode getDriveMode() {
        return driveMode;
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
    }

    public PIDController getAlignableTurnPid() {
        return alignableTurnPid;
    }

    public Pose2d getPose() {
        return poseEstimator.getEstimatedPose();
    }

    public void setGyroOffset(double offset) {
        gyroIO.setAngleOffset(offset);
    }

    public void resetOdometry(Pose2d startingPosition) {
        poseEstimator.resetOdometry(startingPosition.getRotation().getRadians(), startingPosition.getTranslation());
    }

    public Rotation2d getGyroAngle() {
        return Rotation2d.fromDegrees(getLastGyro());
    }
    public ChassisSpeeds getChassisSpeeds(){
        return kinematics.toChassisSpeeds(frontLeft.getLatestState(),frontRight.getLatestState(),backLeft.getLatestState(),backRight.getLatestState());
    }
    public ChassisSpeeds getFieldChassisSpeeds() {
        return ChassisSpeeds.fromRobotRelativeSpeeds(getChassisSpeeds(),getPose().getRotation());
    }
}


