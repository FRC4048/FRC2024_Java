package frc.robot.commands.amp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Amp;
import frc.robot.subsystems.LightStrip;
import frc.robot.utils.TimeoutCounter;

public class DeployAmp extends Command {
    private Amp amp;
    private Timer timeout = new Timer();
    private final TimeoutCounter timeoutCounter;

    public DeployAmp(Amp amp, LightStrip lightStrip) {
        this.amp = amp;
        timeoutCounter = new TimeoutCounter("Deploy Amp", lightStrip);
        addRequirements(amp);
    }

    @Override
    public void initialize() {
        timeout.reset();
        timeout.start();
    }

    @Override
    public void execute() {
        amp.setAmpMotorSpeed(Constants.AMP_MOTOR_SPEED);
    }

    @Override
    public boolean isFinished() {
        if (timeout.hasElapsed(Constants.AMP_TIMEOUT)) {
            timeoutCounter.increaseTimeoutCount();
            return true;
        }
        else {
            return amp.isForwardLimitSwitchPressed();
        }
    }

    @Override
    public void end(boolean interrupted) {
        amp.setAmpMotorSpeed(0);
        amp.setAmpDeployed(true);
    }
}
