package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ramp.Ramp;

public class MoveRamp extends Command {
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
