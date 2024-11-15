package frc.robot.swervev2.encoder;

public interface SwerveAbsEncoder {
    double getAbsolutePosition();
    void zero(double zero);
    double getSteerOffset();
}