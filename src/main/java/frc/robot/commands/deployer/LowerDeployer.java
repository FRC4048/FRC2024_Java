package frc.robot.commands.deployer;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.utils.TimeoutCounter;

public class LowerDeployer extends Command {
    
    private Deployer deployer;
    private Timer timer = new Timer();
    private final double MOTOR_RUN_TIME = Constants.DEPLOYER_RAISE_TIMEOUT;
    private final TimeoutCounter timeoutCounter = new TimeoutCounter("Lower Deployer");

    public LowerDeployer(Deployer deployer) {
        this.deployer = deployer;
        addRequirements(deployer);
    }

    @Override
    public void initialize() {
        //Reset and start timers
        deployer.setDeployerMotorSpeed(Constants.DEPLOYER_RAISE_SPEED);
        timer.reset();
        timer.start();
    }

    @Override 
    public boolean isFinished() {
        //Check is timer has passed timeout point or the deployer has reached the limit switch
        if (deployer.isDeployerReverseLimitSwitchClosed()) {
            return true;
        }
        else if (timer.hasElapsed(MOTOR_RUN_TIME)) {
            timeoutCounter.increaseTimeoutCount();
            return true;
        }
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        deployer.setDeployerMotorSpeed(0);
    }

}
