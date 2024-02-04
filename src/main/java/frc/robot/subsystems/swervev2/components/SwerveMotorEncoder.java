package frc.robot.subsystems.swervev2.components;

public interface SwerveMotorEncoder {
    double getSteerEncPosition();
    double getDriveEncPosition();
    void resetRelEnc();

    double getDriveEncVel();

    double getSteerOffset();

    void setSteerOffset(double zeroAbs);

}
