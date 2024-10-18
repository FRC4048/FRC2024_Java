package frc.robot.commands;

import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableCommand;

public class SetTelopPid extends LoggableCommand {
    private final Ramp ramp;

    public SetTelopPid(Ramp ramp) {
        this.ramp = ramp;
    }

    @Override
    public void initialize() {
        ramp.setFarFF();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
