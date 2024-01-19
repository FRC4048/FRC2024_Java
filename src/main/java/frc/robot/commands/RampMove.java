package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;

public class RampMove extends Command{
    
    private Ramp ramp;
    private double power;

    public RampMove(Ramp ramp, double power) {
        this.ramp = ramp;
        this.power = power;
        addRequirements(this.ramp);
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    @Override
    public void execute() {
        ramp.setRampPos(power);
    }
    
    @Override
    public void end(boolean Interrupted) {
        super.end(Interrupted);
    }

    @Override
    public boolean isFinished() {
            return false;
    }
}
