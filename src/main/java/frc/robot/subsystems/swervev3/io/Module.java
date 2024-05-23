package frc.robot.subsystems.swervev3.io;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.subsystems.LoggableSystem;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.utils.motor.Gain;
import frc.robot.utils.motor.PID;

public class Module {
    private final LoggableSystem<ModuleIO, SwerveModuleInput> system;
    private final PIDController drivePIDController;
    private final ProfiledPIDController turningPIDController;
    private final SimpleMotorFeedforward driveFeedforward;
    private final SimpleMotorFeedforward turnFeedforward;

    public Module(ModuleIO moduleIO, PID drivePid, PID turnPid, Gain driveGain, Gain turnGain, TrapezoidProfile.Constraints goalConstraint, String loggingKey) {
        this.system = new LoggableSystem<>(moduleIO, new SwerveModuleInput(), loggingKey);
        drivePIDController = new PIDController(drivePid.getP(), drivePid.getI(), drivePid.getD());
        turningPIDController = new ProfiledPIDController(turnPid.getP(), turnPid.getI(), turnPid.getD(), goalConstraint);
        driveFeedforward = new SimpleMotorFeedforward(driveGain.getS(), driveGain.getV());
        turnFeedforward = new SimpleMotorFeedforward(turnGain.getS(), turnGain.getV());
        turningPIDController.enableContinuousInput(0, Math.PI * 2);
    }

    public Module(ModuleIO moduleIO, SwervePidConfig pidConfig, String loggingKey) {
        this(moduleIO, pidConfig.getDrivePid(), pidConfig.getSteerPid(), pidConfig.getDriveGain(), pidConfig.getSteerGain(), pidConfig.getGoalConstraint(), loggingKey);
    }

    public void setState(SwerveModuleState desiredState) {
        SwerveModuleState state = SwerveModuleState.optimize(desiredState, new Rotation2d(system.getInputs().steerEncoderPosition));
        double driveSpeed = drivePIDController.calculate(system.getInputs().driveEncoderVelocity, (state.speedMetersPerSecond)) + driveFeedforward.calculate(state.speedMetersPerSecond);
        double turnSpeed = turningPIDController.calculate(system.getInputs().steerEncoderPosition, state.angle.getRadians()) + turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);
        system.getIO().setDriveVoltage(driveSpeed);
        system.getIO().setSteerVoltage(turnSpeed * 12);
    }

    public SwerveModuleState getLatestState() {
        return new SwerveModuleState(system.getInputs().driveEncoderVelocity, new Rotation2d(system.getInputs().steerEncoderPosition));
    }

    public void updateInputs() {
        system.updateInputs();
    }

    public void stop() {
        system.getIO().setDriveVoltage(0);
        system.getIO().setSteerVoltage(0);
    }

    public void resetRelativeEnc() {
        system.getIO().resetEncoder();
    }

    public void setSteerOffset(double steerOffset) {
        system.getIO().setSteerOffset(steerOffset);
    }

    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(system.getInputs().driveEncoderPosition, new Rotation2d(system.getInputs().steerEncoderPosition));
    }
}
