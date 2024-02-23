package frc.robot.commands.climber;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class ManualControlClimber extends Command {

    private Climber climber;
    private DoubleSupplier supplier;
    private BooleanSupplier targetSupplier;
    
    public ManualControlClimber (Climber climber, BooleanSupplier target, DoubleSupplier supplier) {
        this.climber = climber;
        this.targetSupplier = target;
        this.supplier = supplier;
        addRequirements(climber);
    }

    @Override
    public void end(boolean interrupted) {
        climber.setSingleSpeed(true, 0);
        climber.setSingleSpeed(false, 0);
    }

    @Override
    public void execute() {
        double spd = supplier.getAsDouble();
        boolean target = targetSupplier.getAsBoolean();
        climber.setSingleSpeed(target, spd);
    }

    @Override
    public void initialize() {
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    
}
