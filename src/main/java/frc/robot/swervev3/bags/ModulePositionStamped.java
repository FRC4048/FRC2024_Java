package frc.robot.swervev3.bags;

import edu.wpi.first.math.kinematics.SwerveModulePosition;

public record ModulePositionStamped(SwerveModulePosition modulePosition, double timestamp) {

}
