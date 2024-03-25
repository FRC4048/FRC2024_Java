package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.TimeoutCounter;

import java.util.function.DoubleSupplier;

public class RampMove extends Command {

    private final Ramp ramp;
    private final DoubleSupplier encoderValue;
    private final TimeoutCounter timeoutCounter;
    private final Timer timer = new Timer();

    public RampMove(Ramp ramp, LightStrip lightStrip, DoubleSupplier encoderValue) {
        this.ramp = ramp;
        this.encoderValue = encoderValue;
        this.timeoutCounter = new TimeoutCounter("RampMove", lightStrip);
        addRequirements(this.ramp);
    }

    @Override
    public void initialize() {
        timer.restart();
        ramp.setRampPos(encoderValue.getAsDouble());
    }


    @Override
    public void execute() {
        ramp.updateFF();
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        if (timer.hasElapsed(Constants.RAMP_MOVE_TIMEOUT)){
            timeoutCounter.increaseTimeoutCount();
            return true;
        }
        return Math.abs(ramp.getRampPos() - ramp.getDesiredPosition()) < Constants.RAMP_MOVE_THRESHOLD;
    }
}
