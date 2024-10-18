package frc.robot.subsystems.deployer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.LoggableSystem;

public class Deployer extends SubsystemBase{
    private final LoggableSystem<DeployerIO, DeployerInputs> system;

    public Deployer(DeployerIO io) {
        this.system = new LoggableSystem<>(io, new DeployerInputs());
    }

    public void stop() {
        system.getIO().stop();
    }

    @Override
    public void periodic() {
        system.updateInputs();
    }

    //Spin deployer motor
    public void setDeployerMotorSpeed(double speed) {
        system.getIO().setSpeed(speed);
    }

    //Get deployer motor speed
    public double getDeployerMotorSpeed() {
        return system.getInputs().deployerSpeed;
    }

    public boolean isDeployerForwardLimitSwitchClosed() {
        return system.getInputs().fwdLimit;
    }

    public boolean isDeployerReverseLimitSwitchClosed() {
        return system.getInputs().revLimit;
    }

}
