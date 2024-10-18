package frc.robot.commands.ramp;

import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableCommand;

import java.util.function.DoubleSupplier;

public class ManualRamp extends LoggableCommand {
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
