package frc.robot.subsystems.swervev2.components;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;

public interface SwerveMotorEncoder {
    double getSteerEncPosition();
    double getDriveEncPosition();
    void resetRelEnc();

    double getDriveEncVel();

    double getSteerOffset();

    void setSteerOffset(double zeroAbs);

}
