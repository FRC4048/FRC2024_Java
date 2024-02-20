package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;

import java.util.function.DoubleSupplier;

public class ManualRamp extends Command {
    private final Ramp ramp;
    private final DoubleSupplier spd;

    public ManualRamp(Ramp ramp, DoubleSupplier spd) {
        this.ramp = ramp;
        this.spd = spd;
        addRequirements(ramp);
    }

    @Override
    public void execute() {
        ramp.setSpeed(spd.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
