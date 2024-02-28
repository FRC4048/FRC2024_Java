package frc.robot.commands.deployer;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.utils.command.TimedSubsystemCommand;

public class RaiseDeployer extends TimedSubsystemCommand<Deployer> {
    public RaiseDeployer(Deployer deployer) {
        super(deployer,Constants.DEPLOYER_RAISE_TIMEOUT);
    }

    @Override
    public void initialize() {
        super.initialize();
        getSystem().setDeployerMotorSpeed(Constants.DEPLOYER_LOWER_SPEED);
    }

    @Override 
    public boolean isFinished() {
        return getSystem().isDeployerForwardLimitSwitchClosed() || super.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        getSystem().setDeployerMotorSpeed(0);
    }

}
