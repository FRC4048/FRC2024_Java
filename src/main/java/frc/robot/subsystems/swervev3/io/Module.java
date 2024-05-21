package frc.robot.subsystems.swervev3.io;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.subsystems.LoggableSystem;
import frc.robot.subsystems.swervev3.bags.ModulePositionStamped;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.utils.motor.Gain;
import frc.robot.utils.motor.PID;
import org.littletonrobotics.junction.AutoLogOutput;

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
        SwerveModuleState state = SwerveModuleState.optimize(desiredState, new Rotation2d(getLatestSteerEncPos()));
        double driveSpeed = drivePIDController.calculate(getLatestDriveEncVel(), (state.speedMetersPerSecond)) + driveFeedforward.calculate(state.speedMetersPerSecond);
        double turnSpeed = turningPIDController.calculate(getLatestSteerEncPos(), state.angle.getRadians()) + turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);
        system.getIO().setDriveVoltage(driveSpeed);
        system.getIO().setSteerVoltage(turnSpeed * 12);
    }

    public SwerveModuleState getLatestState() {
        return new SwerveModuleState(getLatestDriveEncVel(), new Rotation2d(getLatestSteerEncPos()));
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

    public ModulePositionStamped[] getPositions() {
        ModulePositionStamped[] poses = new ModulePositionStamped[system.getInputs().measurementTimestamps.length];
        for (int i = 0; i < system.getInputs().measurementTimestamps.length; i++) {
            SwerveModulePosition modPos = new SwerveModulePosition(system.getInputs().driveEncoderPosition[i], new Rotation2d(system.getInputs().steerEncoderPosition[i]));
            poses[i] = new ModulePositionStamped(modPos, system.getInputs().measurementTimestamps[i]);
        }
        return poses;
    }

    @AutoLogOutput
    private double getLatestSteerEncPos() {
        if (system.getInputs().steerEncoderPosition.length == 0){
            DriverStation.reportError("Could not find steer encoder value", true);
            return 0;
        }
        return system.getInputs().steerEncoderPosition[system.getInputs().steerEncoderPosition.length - 1];
    }

    @AutoLogOutput
    private double getLatestDriveEncVel() {
        if (system.getInputs().steerEncoderPosition.length == 0){
            DriverStation.reportError("Could not find drive encoder value", true);
            return 0;
        }
        return system.getInputs().driveEncoderVelocity[system.getInputs().steerEncoderPosition.length - 1];
    }
}
