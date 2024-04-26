package frc.robot.subsystems.deployer;


public interface DeployerIO {
    void stop();
    void setSpeed(double spd);
    void updateInputs(DeployerInputs inputs);

}
