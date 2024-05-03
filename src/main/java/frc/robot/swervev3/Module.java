package frc.robot.swervev3;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.swervev2.SwervePidConfig;
import frc.robot.utils.Gain;
import frc.robot.utils.PID;
import org.littletonrobotics.junction.Logger;

public class Module {
    private final ModuleIO moduleIO;
    private final SwerveModuleInput inputs = new SwerveModuleInput();
    private final PIDController drivePIDController;
    private final String loggingKey;
    private final ProfiledPIDController turningPIDController;
    private final SimpleMotorFeedforward driveFeedforward;
    private final SimpleMotorFeedforward turnFeedforward;

    public Module(ModuleIO moduleIO, PID drivePid, PID turnPid, Gain driveGain, Gain turnGain, TrapezoidProfile.Constraints goalConstraint, String loggingKey) {
        this.moduleIO = moduleIO;
        drivePIDController = new PIDController(drivePid.getP(),drivePid.getI(),drivePid.getD());
        this.loggingKey = loggingKey;
        turningPIDController = new ProfiledPIDController(turnPid.getP(),turnPid.getI(),turnPid.getD(),goalConstraint);
        driveFeedforward = new SimpleMotorFeedforward(driveGain.getS(),driveGain.getV());
        turnFeedforward = new SimpleMotorFeedforward(turnGain.getS(),turnGain.getV());
        turningPIDController.enableContinuousInput(0, Math.PI * 2);
    }
    public Module(ModuleIO moduleIO, SwervePidConfig pidConfig, String loggingKey) {
        this(moduleIO, pidConfig.getDrivePid(), pidConfig.getSteerPid(), pidConfig.getDriveGain(), pidConfig.getSteerGain(), pidConfig.getGoalConstraint(), loggingKey);
    }

    public void setState(SwerveModuleState desiredState){
        SwerveModuleState state = SwerveModuleState.optimize(desiredState, new Rotation2d(inputs.steerEncoderPosition));
        double driveSpeed = drivePIDController.calculate(inputs.driveEncoderVelocity,(state.speedMetersPerSecond)) + driveFeedforward.calculate(state.speedMetersPerSecond);
        double turnSpeed = turningPIDController.calculate(inputs.steerEncoderPosition, state.angle.getRadians()) + turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);
        moduleIO.setDriveVoltage(driveSpeed);
        moduleIO.setSteerVoltage(turnSpeed * 12);
    }

    public SwerveModuleState getState() {
        return new SwerveModuleState(inputs.driveEncoderVelocity,new Rotation2d(inputs.steerEncoderPosition));
    }
    public void processInputs(){
        moduleIO.updateInputs(inputs);
        Logger.processInputs(loggingKey + "Inputs", inputs);
    }
    public void stop(){
        moduleIO.setDriveVoltage(0);
        moduleIO.setSteerVoltage(0);
    }

    public void resetRelativeEnc() {
        moduleIO.resetEncoder();
    }
    public void setSteerOffset(double steerOffset){
        moduleIO.setSteerOffset(steerOffset);
    }
    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(inputs.driveEncoderPosition,new Rotation2d(inputs.steerEncoderPosition));
    }
}
