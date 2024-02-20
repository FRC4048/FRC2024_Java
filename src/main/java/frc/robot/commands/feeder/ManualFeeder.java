package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder;

import java.util.function.DoubleSupplier;

public class ManualFeeder extends Command {
    private final DoubleSupplier spd;
    private final Feeder feeder;

    protected ManualFeeder(Feeder feeder, DoubleSupplier spd) {
        this.feeder = feeder;
        this.spd = spd;
        addRequirements(feeder);
    }

    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(spd.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
