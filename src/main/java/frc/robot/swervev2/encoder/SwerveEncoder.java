package frc.robot.swervev2.encoder;

public interface SwerveEncoder {
    void setPosition(double pos);
    double getPosition();
    double getVelocity();
}