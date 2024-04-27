package frc.robot.commands.climber;

import edu.wpi.first.math.MathUtil;
import frc.robot.subsystems.climber.Climber;
import frc.robot.utils.loggingv2.LoggableCommand;

import java.util.function.DoubleSupplier;

public class ManualControlClimber extends LoggableCommand {

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
        double value = MathUtil.applyDeadband(supplier.getAsDouble(),0.2);
        if (value > 0){
            climber.disengageRatchet();
        } else if (value < 0) {
            climber.engageRatchet();
        }
        climber.setSpeed(value);
    }

    @Override
    public boolean isFinished() {
        return false;
    }    
}
