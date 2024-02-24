package frc.robot.commands.deployer;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.IntakeSubsystem;

public class RaiseAndLowerDeployer extends Command{
    private Deployer deployer;
    private boolean deployerUp;
    private double time;

    public RaiseAndLowerDeployer(Deployer deployer) {
        this.deployer = deployer;
        addRequirements(deployer);
    }

   @Override
    public void end(boolean interrupted) {
    }

    @Override
    public void execute() {
        if (deployerUp == true) {
            deployer.setDeployerMotorSpeed(Constants.DEPLOYER_LOWER_SPEED);
        } else {
            deployer.setDeployerMotorSpeed(Constants.DEPLOYER_RAISE_SPEED);
        }
    }

    @Override
    public void initialize() {
        deployerUp = deployer.isDeployerFowardLimitSwitchClosed();
        time = Timer.getFPGATimestamp();
    } 

    @Override
    public boolean isFinished() {
        if (deployerUp == true) {
            return deployer.isDeployerReverseLimitSwitchClosed() || Timer.getFPGATimestamp() - time == 5;
        } else {
            return deployer.isDeployerFowardLimitSwitchClosed() || Timer.getFPGATimestamp() - time == 5;
        }
    }
}
