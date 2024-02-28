package frc.robot.commands.ramp;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Ramp;

public class RampMove extends Command{
    
    private final Ramp ramp;
    private final DoubleSupplier encoderValue;

    public RampMove(Ramp ramp, DoubleSupplier encoderValue) {
        this.ramp = ramp;
        this.encoderValue = encoderValue;
        addRequirements(this.ramp);
    }

    @Override
    public void initialize() {
        ramp.setRampPos(encoderValue.getAsDouble());
    }


    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        return (Math.abs(ramp.getRampPos() - encoderValue.getAsDouble()) < Constants.RAMP_MOVE_TRESHOLD);
    }
}
