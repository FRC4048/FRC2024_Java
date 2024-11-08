package frc.robot.swervev2.encoder;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class SwerveTalonEncoder implements SwerveEncoder {
    private final WPI_TalonSRX motor;
    private final double positionConversionFactor;
    private final double velocityConversionFactor;

    public SwerveTalonEncoder(WPI_TalonSRX motor, double velFactor, double posFactor) {
        this.motor = motor;
        positionConversionFactor = posFactor;
        velocityConversionFactor = velFactor;
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
