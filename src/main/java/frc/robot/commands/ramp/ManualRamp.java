package frc.robot.commands.ramp;

import frc.robot.subsystems.Ramp;
import frc.robot.utils.command.SubsystemCommandBase;

import java.util.function.DoubleSupplier;

public class ManualRamp extends SubsystemCommandBase<Ramp> {
    private final DoubleSupplier spd;

    public ManualRamp(Ramp ramp, DoubleSupplier spd) {
        super(ramp);
        this.spd = spd;
    }

    @Override
    public void execute() {
        getSystem().setSpeed(spd.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
