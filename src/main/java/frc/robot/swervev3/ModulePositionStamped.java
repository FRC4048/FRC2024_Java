package frc.robot.swervev3;

import edu.wpi.first.math.kinematics.SwerveModulePosition;

public record ModulePositionStamped(SwerveModulePosition modulePosition, double timestamp) {

}
