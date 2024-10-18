package frc.robot.subsystems.deployer;


import frc.robot.subsystems.LoggableIO;

public interface DeployerIO extends LoggableIO<DeployerInputs> {
    void stop();
    void setSpeed(double spd);
}
