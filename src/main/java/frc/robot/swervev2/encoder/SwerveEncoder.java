package frc.robot.swervev2.encoder;

public interface SwerveEncoder {
    void setPositionConversionFactor(double factor);
    void setVelocityConversionFactor(double factor);
    void setPosition(double pos);
    double getPosition();
    double getVelocity();
}
