package frc.robot.swervev2.encoder;

import com.revrobotics.RelativeEncoder;

public class SwerveSparkMaxEncoder implements SwerveEncoder {
    private final RelativeEncoder encoder;

    public SwerveSparkMaxEncoder(RelativeEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public void setPositionConversionFactor(double factor) {
        encoder.setPositionConversionFactor(factor);
    }

    @Override
    public void setVelocityConversionFactor(double factor) {
        encoder.setVelocityConversionFactor(factor);
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
