package frc.robot.commands.amp;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Amp;
import frc.robot.utils.command.TimedSubsystemCommand;

public class DeployAmp extends TimedSubsystemCommand<Amp> {
    public DeployAmp(Amp amp) {
        super(amp,Constants.AMP_TIMEOUT);
    }

    @Override
    public void execute() {
        getSystem().setAmpMotorSpeed(Constants.AMP_MOTOR_SPEED);
    }

    @Override
    public boolean isFinished() {
       return getSystem().isForwardLimitSwitchPressed() || super.isFinished();
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        getSystem().setAmpMotorSpeed(0);
    }
}
