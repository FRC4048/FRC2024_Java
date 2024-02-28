package frc.robot.commands.amp;

        import edu.wpi.first.wpilibj.Timer;
        import edu.wpi.first.wpilibj2.command.Command;
        import frc.robot.constants.Constants;
        import frc.robot.subsystems.Amp;
        import frc.robot.utils.TimeoutCounter;

public class ToggleAmp extends Command {
    private Amp amp;
    private Timer timeout = new Timer();
    private double speed;
    private final TimeoutCounter timeoutCounter = new TimeoutCounter("ToggleAmp");

    public ToggleAmp(Amp amp) {
        this.amp = amp;
        addRequirements(amp);
    }

    @Override
    public void initialize() {
        timeout.reset();
        timeout.start();
        speed = Constants.AMP_MOTOR_SPEED;
        if (amp.isAmpDeployed()) {
            speed *= -1;
        }
    }

    @Override
    public void execute() {
        amp.setAmpMotorSpeed(speed);
    }

    @Override
    public boolean isFinished() {
        if (!amp.isAmpDeployed() && amp.isForwardLimitSwitchPressed()) {
            return true;
        } else if (amp.isAmpDeployed() && amp.isReverseLimitSwitchPressed()) {
            return true;
        }
        else {
            timeoutCounter.increaseTimeoutCount();
            return(timeout.hasElapsed((Constants.AMP_TIMEOUT)));
        }
    }

    @Override
    public void end(boolean interrupted) {
        amp.setAmpMotorSpeed(0);
        amp.setAmpDeployed(!amp.isAmpDeployed());
    }
}
