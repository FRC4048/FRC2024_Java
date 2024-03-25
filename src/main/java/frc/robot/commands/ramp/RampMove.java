package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;

public class RampMove extends Command{
    
    private final Ramp ramp;
    private final double encoderValue;

    public RampMove(Ramp ramp, double encoderValue) {
        this.ramp = ramp;
        this.encoderValue = encoderValue;
        addRequirements(this.ramp);
    }

    @Override
    public void initialize() {
        ramp.setRampPos(encoderValue);
    }


    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
            return true;
    }
}
