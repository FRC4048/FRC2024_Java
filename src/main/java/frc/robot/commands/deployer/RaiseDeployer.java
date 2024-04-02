package frc.robot.commands.deployer;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.LightStrip;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.TimeoutCounter;

public class RaiseDeployer extends Command {
    
    private Deployer deployer;
    private Timer timer = new Timer();
    private final double MOTOR_RUN_TIME = Constants.DEPLOYER_LOWER_TIMEOUT;
    private final TimeoutCounter timeoutCounter;
    private LightStrip lightStrip;

    public RaiseDeployer(Deployer deployer, LightStrip lightStrip) {
        this.deployer = deployer;
        this.timeoutCounter = new TimeoutCounter("Raise Deployer", lightStrip);
        this.lightStrip = lightStrip;
        addRequirements(deployer);
    }

    @Override
    public void initialize() {
        //Reset and start timers
        deployer.setDeployerMotorSpeed(Constants.DEPLOYER_LOWER_SPEED);
        lightStrip.setPattern(BlinkinPattern.BLACK);
        timer.reset();
        timer.start();
    }

    @Override 
    public boolean isFinished() {
        //Check is timer has passed timeout point or if deployer has reached limit switch
        if (deployer.isDeployerForwardLimitSwitchClosed()) {
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
