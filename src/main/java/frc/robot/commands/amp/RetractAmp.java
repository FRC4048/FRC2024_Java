package frc.robot.commands.amp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Amp;

public class RetractAmp extends Command {
    private Amp amp;
    private Timer timeout = new Timer();

    public RetractAmp(Amp amp) {
        this.amp = amp;
        addRequirements(amp);
    }

    @Override
    public void initialize() {
        timeout.reset();
        timeout.start();
    }

    @Override
    public void execute() {
        amp.setAmpMotorSpeed(-1*Constants.AMP_MOTOR_SPEED);
        amp.setAmpDeployed(false);
    }

    @Override
    public boolean isFinished() {
        return (timeout.hasElapsed(Constants.AMP_TIMEOUT) || amp.isReverseLimitSwitchPressed());
    }

    @Override
    public void end(boolean interrupted) {
        amp.setAmpMotorSpeed(0);
    }
}
