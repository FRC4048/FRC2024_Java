package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;

import java.util.function.DoubleSupplier;

public class RampFollow extends Command {
    private final Ramp ramp;
    private final DoubleSupplier targetValue;

    protected RampFollow(Ramp ramp, DoubleSupplier targetValue) {
        this.ramp = ramp;
        this.targetValue = targetValue;
        addRequirements(ramp);
    }

    @Override
    public void execute() {
        ramp.setRampPos(targetValue.getAsDouble());
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
