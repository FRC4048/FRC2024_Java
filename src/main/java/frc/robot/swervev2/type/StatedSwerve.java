package frc.robot.swervev2.type;

import edu.wpi.first.math.kinematics.SwerveModuleState;

public interface StatedSwerve {

    /**
     * @return the current State of a serveModule
     * @see SwerveModuleState
     */
    SwerveModuleState getState();

    /**
     * @param desiredState the target state
     * @see SwerveModuleState
     */
    void setDesiredState(SwerveModuleState desiredState);
}