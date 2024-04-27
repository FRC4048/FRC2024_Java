package frc.robot.commands.pathplanning;

import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.loggingv2.LoggableCommand;

public class MoveRamp extends LoggableCommand {
    private final Ramp ramp;
    private final double encoderValue;

    public MoveRamp(Ramp ramp, double encoderValue) {
        this.ramp = ramp;
        this.encoderValue = encoderValue;
        addRequirements(this.ramp);
    }

    @Override
    public void initialize() {
        ramp.setRampPos(encoderValue);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
