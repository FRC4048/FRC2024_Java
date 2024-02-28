package frc.robot.commands.amp;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Amp;
import frc.robot.utils.command.TimedSubsystemCommand;

public class RetractAmp extends TimedSubsystemCommand<Amp> {

    public RetractAmp(Amp amp) {
        super(amp,Constants.AMP_TIMEOUT);
    }

    @Override
    public void execute() {
        getSystem().setAmpMotorSpeed(-1 * Constants.AMP_MOTOR_SPEED);
    }

    @Override
    public boolean isFinished() {
        return getSystem().isReverseLimitSwitchPressed() || super.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        getSystem().setAmpMotorSpeed(0);
    }
}
