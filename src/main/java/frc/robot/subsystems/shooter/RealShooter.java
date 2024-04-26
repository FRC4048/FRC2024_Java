package frc.robot.subsystems.shooter;

import com.revrobotics.CANSparkBase;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.utils.NeoPidMotor;

public class RealShooter implements ShooterIO {
    private final NeoPidMotor neoPidMotorLeft;
    private final NeoPidMotor neoPidMotorRight;

    public RealShooter() {
        neoPidMotorLeft = new NeoPidMotor(Constants.SHOOTER_MOTOR_LEFT);
        neoPidMotorRight = new NeoPidMotor(Constants.SHOOTER_MOTOR_RIGHT);
        configureMotor();
    }

    private void configureMotor() {
        neoPidMotorLeft.setPid(GameConstants.SHOOTER_PID_P, GameConstants.SHOOTER_PID_I, GameConstants.SHOOTER_PID_D, GameConstants.SHOOTER_PID_FF);
        neoPidMotorRight.setPid(GameConstants.SHOOTER_PID_P, GameConstants.SHOOTER_PID_I, GameConstants.SHOOTER_PID_D, GameConstants.SHOOTER_PID_FF);

        neoPidMotorLeft.setMaxAccel(Constants.SHOOTER_MAX_RPM_ACCELERATION);
        neoPidMotorRight.setMaxAccel(Constants.SHOOTER_MAX_RPM_ACCELERATION);

        neoPidMotorLeft.setMinMaxVelocity(30000, 0);
        neoPidMotorRight.setMinMaxVelocity(30000, 0);

        neoPidMotorLeft.setInverted(false);
        neoPidMotorRight.setInverted(true);

        neoPidMotorLeft.setIdleMode(CANSparkBase.IdleMode.kBrake);
        neoPidMotorRight.setIdleMode(CANSparkBase.IdleMode.kBrake);
    }
    @Override
    public void slowStop() {
        neoPidMotorLeft.getNeoMotor().set(0);
        neoPidMotorRight.getNeoMotor().set(0);
    }

    @Override
    public void stop() {
        neoPidMotorLeft.setPidSpeed(0);
        neoPidMotorRight.setPidSpeed(0);
    }

    @Override
    public void setShooterLeftRPM(double rpm) {
        neoPidMotorLeft.setPidSpeed(rpm);
    }

    @Override
    public void setShooterRightRPM(double rpm) {
        neoPidMotorRight.setPidSpeed(rpm);
    }

    @Override
    public void updateInputs(ShooterInputs inputs) {
        inputs.shooterMotorLeftRPM = neoPidMotorLeft.getCurrentSpeed();
        inputs.shooterMotorRightRPM = neoPidMotorRight.getCurrentSpeed();
    }
}
