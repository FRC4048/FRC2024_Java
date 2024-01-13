package frc.robot.subsystems.swervev2.type;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

public class GenericSwerveModule implements StatedSwerve {
    private final PIDController drivePIDController;
    private final ProfiledPIDController turningPIDController;
    private final SimpleMotorFeedforward driveFeedforward;
    private final SimpleMotorFeedforward turnFeedforward;
    private final GenericEncodedSwerve swerveMotor;

    public GenericSwerveModule(GenericEncodedSwerve swerveMotor, PID drivePid, PID turnPid, Gain driveGain, Gain turnGain, TrapezoidProfile.Constraints goalConstraint) {
        this.swerveMotor = swerveMotor;
        drivePIDController = new PIDController(drivePid.getP(),drivePid.getI(),drivePid.getD());
        turningPIDController = new ProfiledPIDController(turnPid.getP(),turnPid.getI(),turnPid.getD(),goalConstraint);
        driveFeedforward = new SimpleMotorFeedforward(driveGain.getS(),driveGain.getV());
        turnFeedforward = new SimpleMotorFeedforward(turnGain.getS(),turnGain.getV());
        turningPIDController.enableContinuousInput(0, Math.PI * 2);
    }

    @Override
    public SwerveModuleState getState() {
        return new SwerveModuleState(swerveMotor.getDriveEncVel(),new Rotation2d(swerveMotor.getSteerEncPosition()));
    }

    @Override
    public void setDesiredState(SwerveModuleState desiredState) {
        SwerveModuleState state = SwerveModuleState.optimize(desiredState, new Rotation2d(swerveMotor.getSteerEncPosition()));
        double driveSpeed = calcDrivePidOut(state.speedMetersPerSecond) + calcDriveFeedForward(state.speedMetersPerSecond);
        double turnSpeed = calcSteerPidOut(state.angle.getRadians()) + calcSteerFeedForward();
        swerveMotor.getDriveMotor().setVoltage(driveSpeed);
        swerveMotor.getSteerMotor().set(turnSpeed);
    }

    
    private double calcDrivePidOut(double setpoint) {
        return drivePIDController.calculate(swerveMotor.getDriveEncVel(), setpoint);
    }
    
    private double calcSteerPidOut(double goal) {
        return turningPIDController.calculate(swerveMotor.getSteerEncPosition(), goal);
    }
    
    private double calcDriveFeedForward(double velocity) {
        return driveFeedforward.calculate(velocity);
    }
    
    private double calcSteerFeedForward() {
        return turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);
    }
    public GenericEncodedSwerve getSwerveMotor(){
        return swerveMotor;
    }
}
