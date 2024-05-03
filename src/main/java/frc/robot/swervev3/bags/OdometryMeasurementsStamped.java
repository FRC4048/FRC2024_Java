package frc.robot.swervev3.bags;

import edu.wpi.first.math.kinematics.SwerveModulePosition;

public record OdometryMeasurementsStamped(SwerveModulePosition[] modulePosition, double gyroValueDeg, double timestamp) {
}
