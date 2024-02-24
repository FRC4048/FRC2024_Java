package frc.robot.commands.climber;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class ManualControlClimber extends Command {

    private Climber climber;
    private DoubleSupplier supplier;
    
    public ManualControlClimber (Climber climber, DoubleSupplier supplier) {
        this.climber = climber;
        this.supplier = supplier;
        addRequirements(climber);
    }

    @Override
    public void end(boolean interrupted) {
        climber.setSpeed(0);
    }

    @Override
    public void execute() {
        double value = supplier.getAsDouble();
        if (Math.abs(value) > .2) {
            climber.setSpeed(supplier.getAsDouble());
        } else {
            climber.setSpeed(0);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }    
}
