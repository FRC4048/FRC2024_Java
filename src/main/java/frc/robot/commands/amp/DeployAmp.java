package frc.robot.commands.amp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Amp;

public class DeployAmp extends Command {
    private Amp amp;
    private Timer timeout = new Timer();

    public DeployAmp(Amp amp) {
        this.amp = amp;
        addRequirements(amp);
    }

    @Override
    public void initialize() {
        timeout.start();
    }

    @Override
    public void execute() {
        amp.setAmpMotorSpeed(Constants.AMP_MOTOR_SPEED);
    }

    @Override
    public boolean isFinished() {
        return (timeout.hasElapsed(Constants.AMP_TIMEOUT) || amp.isForwardLimitSwitchPressed());
    }

    @Override
    public void end(boolean interrupted) {
        amp.setAmpMotorSpeed(0);
        timeout.reset();
    }
}
