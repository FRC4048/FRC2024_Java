package frc.robot.swervev2.encoder;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class SwerveSparkMaxEncoder implements SwerveEncoder {
    private final RelativeEncoder encoder;

    public SwerveSparkMaxEncoder(CANSparkMax motor, double velFactor, double posFactor) {
        this.encoder = motor.getEncoder();
        this.encoder.setVelocityConversionFactor(velFactor);
        this.encoder.setPositionConversionFactor(posFactor);
    }

    @Override
    public void setPosition(double pos) {
        encoder.setPosition(pos);
    }

    @Override
    public double getPosition() {
        return encoder.getPosition();
    }

    @Override
    public double getVelocity() {
        return encoder.getVelocity();
    }
}