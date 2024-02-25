package frc.robot.commands.climber;

import frc.robot.subsystems.Climber;
import frc.robot.utils.command.SubsystemCommandBase;

import java.util.function.DoubleSupplier;

public class ManualControlClimber extends SubsystemCommandBase<Climber> {
    private final DoubleSupplier supplier;
    
    public ManualControlClimber (Climber climber, DoubleSupplier supplier) {
        super(climber);
        this.supplier = supplier;
    }

    @Override
    public void end(boolean interrupted) {
        getSystem().setSpeed(0);
    }

    @Override
    public void execute() {
        double value = supplier.getAsDouble();
        if (Math.abs(value) > .2) {
            getSystem().setSpeed(supplier.getAsDouble());
        } else {
            getSystem().setSpeed(0);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }    
}
