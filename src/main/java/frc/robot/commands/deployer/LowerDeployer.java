package frc.robot.commands.deployer;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.utils.TimeoutCounter;
import frc.robot.utils.loggingv2.LoggableCommand;

public class LowerDeployer extends LoggableCommand {
    
    private Deployer deployer;
    private Timer timer = new Timer();
    private final double MOTOR_RUN_TIME = Constants.DEPLOYER_RAISE_TIMEOUT;
    private final TimeoutCounter timeoutCounter;

    public LowerDeployer(Deployer deployer, LightStrip lightStrip) {
        this.deployer = deployer;
        timeoutCounter = new TimeoutCounter("Lower Deployer", lightStrip);
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
