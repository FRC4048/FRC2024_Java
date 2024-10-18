package frc.robot.commands.ramp;

import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableCommand;

public class SetAutoRampPid extends LoggableCommand {

    private final Ramp ramp;

    public SetAutoRampPid(Ramp ramp) {
        this.ramp = ramp;
    }

    @Override
    public void initialize() {
        ramp.setDefaultFF();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
