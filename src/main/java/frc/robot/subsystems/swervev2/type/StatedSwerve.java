package frc.robot.subsystems.swervev2.type;

import edu.wpi.first.math.kinematics.SwerveModuleState;

public interface StatedSwerve {

    SwerveModuleState getState();

    void setDesiredState(SwerveModuleState desiredState);
}
