package frc.robot.commands.deployer;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.utils.command.TimedSubsystemCommand;

public class LowerDeployer extends TimedSubsystemCommand<Deployer> {

    public LowerDeployer(Deployer deployer) {
        super(deployer,Constants.DEPLOYER_LOWER_TIMEOUT);
    }

    @Override
    public void initialize() {
        super.initialize();
        getSystem().setDeployerMotorSpeed(Constants.DEPLOYER_RAISE_SPEED);
    }

    @Override 
    public boolean isFinished() {
       return getSystem().isDeployerReverseLimitSwitchClosed() || super.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        getSystem().setDeployerMotorSpeed(0);
    }

}
