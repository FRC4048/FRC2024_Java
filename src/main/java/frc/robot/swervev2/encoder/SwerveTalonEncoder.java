package frc.robot.swervev2.encoder;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class SwerveTalonEncoder implements SwerveEncoder {
    private final WPI_TalonSRX motor;
    private double positionConversionFactor;
    private double velocityConversionFactor;

    public SwerveTalonEncoder(WPI_TalonSRX motor) {
        this.motor = motor;
    }

    @Override
    public void setPositionConversionFactor(double factor) {
        positionConversionFactor = factor;
    }

    @Override
    public void setVelocityConversionFactor(double factor) {
        velocityConversionFactor = factor;
    }

    @Override
    public void setPosition(double pos) {
        motor.setSelectedSensorPosition(pos / positionConversionFactor);
    }

    @Override
    public double getPosition() {
        return motor.getSelectedSensorPosition() * positionConversionFactor;
    }

    @Override
    public double getVelocity() {
        return motor.getSelectedSensorVelocity() * velocityConversionFactor;
    }
}
