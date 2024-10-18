package frc.robot.commands.ramp;

import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableCommand;

import java.util.function.DoubleSupplier;

public class RampMove extends LoggableCommand {
    
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
            return true;
    }
}
