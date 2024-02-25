package frc.robot.commands.feeder;

import frc.robot.subsystems.Feeder;
import frc.robot.utils.command.SubsystemCommandBase;

import java.util.function.DoubleSupplier;

public class ManualFeeder extends SubsystemCommandBase<Feeder> {
    private final DoubleSupplier spd;

    public ManualFeeder(Feeder feeder, DoubleSupplier spd) {
        super(feeder);
        this.spd = spd;
    }

    @Override
    public void execute() {
        getSystem().setFeederMotorSpeed(spd.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
