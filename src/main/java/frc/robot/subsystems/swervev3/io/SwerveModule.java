package frc.robot.subsystems.swervev3.io;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.subsystems.LoggableSystem;
import frc.robot.subsystems.swervev3.KinematicsConversionConfig;
import frc.robot.subsystems.swervev3.io.abs.CANCoderAbsIO;
import frc.robot.subsystems.swervev3.io.abs.SwerveAbsIO;
import frc.robot.subsystems.swervev3.io.abs.SwerveAbsInput;
import frc.robot.subsystems.swervev3.io.drive.SparkMaxDriveMotorIO;
import frc.robot.subsystems.swervev3.io.drive.SwerveDriveMotorIO;
import frc.robot.subsystems.swervev3.io.drive.SwerveDriveMotorInput;
import frc.robot.subsystems.swervev3.io.steer.SparkMaxSteerMotorIO;
import frc.robot.subsystems.swervev3.io.steer.SwerveSteerMotorIO;
import frc.robot.subsystems.swervev3.io.steer.SwerveSteerMotorInput;
import frc.robot.swervev2.SwerveIdConfig;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.utils.motor.Gain;
import frc.robot.utils.motor.PID;

public class SwerveModule {
    private final LoggableSystem<SwerveDriveMotorIO, SwerveDriveMotorInput> driveSystem;
    private final LoggableSystem<SwerveSteerMotorIO, SwerveSteerMotorInput> steerSystem;
    private final LoggableSystem<SwerveAbsIO, SwerveAbsInput> absSystem;
    private final PIDController drivePIDController;
    private final ProfiledPIDController turningPIDController;
    private final SimpleMotorFeedforward driveFeedforward;
    private final SimpleMotorFeedforward turnFeedforward;

    public SwerveModule(SwerveDriveMotorIO driveMotorIO, SwerveSteerMotorIO steerMotorIO, SwerveAbsIO absIO, PID drivePid, PID turnPid, Gain driveGain, Gain turnGain, TrapezoidProfile.Constraints goalConstraint, String loggingKey) {
        this.driveSystem = new LoggableSystem<>(driveMotorIO, new SwerveDriveMotorInput(), loggingKey);
        this.steerSystem = new LoggableSystem<>(steerMotorIO, new SwerveSteerMotorInput(), loggingKey);
        this.absSystem = new LoggableSystem<>(absIO, new SwerveAbsInput(), loggingKey);
        drivePIDController = new PIDController(drivePid.getP(), drivePid.getI(), drivePid.getD());
        turningPIDController = new ProfiledPIDController(turnPid.getP(), turnPid.getI(), turnPid.getD(), goalConstraint);
        driveFeedforward = new SimpleMotorFeedforward(driveGain.getS(), driveGain.getV());
        turnFeedforward = new SimpleMotorFeedforward(turnGain.getS(), turnGain.getV());
        turningPIDController.enableContinuousInput(0, Math.PI * 2);
    }

    public SwerveModule(SwerveDriveMotorIO driveMotorIO, SwerveSteerMotorIO steerMotorIO, SwerveAbsIO absIO, SwervePidConfig pidConfig, String loggingKey) {
        this(driveMotorIO, steerMotorIO, absIO, pidConfig.getDrivePid(), pidConfig.getSteerPid(), pidConfig.getDriveGain(), pidConfig.getSteerGain(), pidConfig.getGoalConstraint(), loggingKey);
    }

    public void setState(SwerveModuleState desiredState) {
        SwerveModuleState state = SwerveModuleState.optimize(desiredState, new Rotation2d(steerSystem.getInputs().steerEncoderPosition));
        double driveSpeed = drivePIDController.calculate(driveSystem.getInputs().driveEncoderVelocity, (state.speedMetersPerSecond)) + driveFeedforward.calculate(state.speedMetersPerSecond);
        double turnSpeed = turningPIDController.calculate(steerSystem.getInputs().steerEncoderPosition, state.angle.getRadians()) + turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);
        driveSystem.getIO().setDriveVoltage(driveSpeed);
        steerSystem.getIO().setSteerVoltage(turnSpeed * 12);
    }

    public SwerveModuleState getLatestState() {
        return new SwerveModuleState(driveSystem.getInputs().driveEncoderVelocity, new Rotation2d(steerSystem.getInputs().steerEncoderPosition));
    }

    public void updateInputs() {
        absSystem.updateInputs();
        driveSystem.updateInputs();
        steerSystem.updateInputs();
    }

    public void stop() {
        driveSystem.getIO().setDriveVoltage(0);
        steerSystem.getIO().setSteerVoltage(0);
    }

    public void resetRelativeEnc() {
        driveSystem.getIO().resetEncoder();
        steerSystem.getIO().resetEncoder();
    }

    public void setSteerOffset(double steerOffset) {
        steerSystem.getIO().setSteerOffset(steerOffset, absSystem.getInputs().absEncoderPosition);
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(driveSystem.getInputs().driveEncoderPosition, new Rotation2d(steerSystem.getInputs().steerEncoderPosition));
    }

    public static SwerveModule createModule(SwerveIdConfig idConf, KinematicsConversionConfig kinematicsConfig, SwervePidConfig pidConfig, String loggingKey){
        SparkMaxDriveMotorIO frontLeftDriveMotorIO = new SparkMaxDriveMotorIO(idConf.getDriveMotorId(), kinematicsConfig, kinematicsConfig.getProfile().isFrontLeftInverted());
        SparkMaxSteerMotorIO frontLeftSteerMotorIO = new SparkMaxSteerMotorIO(idConf.getTurnMotorId(), kinematicsConfig, kinematicsConfig.getProfile().isSteerInverted());
        CANCoderAbsIO frontLeftAbsIO = new CANCoderAbsIO(idConf.getCanCoderId());
        return new SwerveModule(frontLeftDriveMotorIO, frontLeftSteerMotorIO, frontLeftAbsIO, pidConfig, loggingKey);
    }
}
