package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;

public class SetAutoRampPid extends Command {

    private final Ramp ramp;

    public SetAutoRampPid(Ramp ramp) {
        this.ramp = ramp;
    }

    @Override
    public void initialize() {
        ramp.restorePid();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
