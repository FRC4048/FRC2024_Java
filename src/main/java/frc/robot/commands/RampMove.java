package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;

public class RampMove extends Command{
    
    private Ramp ramp;
    private double degrees;

    public RampMove(Ramp ramp, double degrees) {
        this.ramp = ramp;
        this.degrees = degrees;
        addRequirements(this.ramp);
    }

    @Override
    public void initialize() {
        ramp.setRampPos(degrees);
    }


    @Override
    public boolean isFinished() {
            return true;
    }
}
