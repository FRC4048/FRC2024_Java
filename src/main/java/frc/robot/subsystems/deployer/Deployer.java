package frc.robot.subsystems.deployer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Deployer extends SubsystemBase{
    private final DeployerIO deployerIO;
    private final DeployerInputs inputs = new DeployerInputs();

    public Deployer() {
        deployerIO = new RealDeployer();
    }

    public void stop() {
        deployerIO.stop();
    }

    @Override
    public void periodic() {
        deployerIO.updateInputs(inputs);
        org.littletonrobotics.junction.Logger.processInputs("climberInputs", inputs);
    }

    //Spin deployer motor
    public void setDeployerMotorSpeed(double speed) {
        deployerIO.setSpeed(speed);
    }

    //Get deployer motor speed
    public double getDeployerMotorSpeed() {
        return inputs.deployerSpeed;
    }

    public boolean isDeployerForwardLimitSwitchClosed() {
        return inputs.fwdLimit;
    }

    public boolean isDeployerReverseLimitSwitchClosed() {
        return inputs.revLimit;
    }

}
