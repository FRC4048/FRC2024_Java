package frc.robot.commands.ramp;

import frc.robot.subsystems.Ramp;
import frc.robot.utils.command.SubsystemCommandBase;

import java.util.function.DoubleSupplier;

public class RampMove extends SubsystemCommandBase<Ramp> {
    private final DoubleSupplier encoderValue;

    public RampMove(Ramp ramp, DoubleSupplier encoderValue) {
        super(ramp);
        this.encoderValue = encoderValue;
    }

    @Override
    public void initialize() {
        super.initialize();
        getSystem().setRampPos(encoderValue.getAsDouble());
    }


    @Override
    public void execute() {
        getSystem().setRampPos(encoderValue.getAsDouble());
    }

    @Override
    public boolean isFinished() {
            return true;
    }
}
