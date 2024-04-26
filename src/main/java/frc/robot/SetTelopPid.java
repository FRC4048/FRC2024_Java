package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ramp.Ramp;

public class SetTelopPid extends Command {
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
