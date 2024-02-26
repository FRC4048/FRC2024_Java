package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.TimeoutCounter;

import java.util.function.DoubleSupplier;

public class RampMoveAndWait extends Command{

    private final Ramp ramp;
    private final DoubleSupplier encoderValue;
    private final Timer timer = new Timer();
    private final TimeoutCounter timeoutCounter = new TimeoutCounter("Wait Ramp");

    public RampMoveAndWait(Ramp ramp, DoubleSupplier encoderValue) {
        this.ramp = ramp;
        this.encoderValue = encoderValue;
        addRequirements(this.ramp);
    }

    @Override
    public void initialize() {
        ramp.setRampPos(encoderValue.getAsDouble());
        timer.reset();
        timer.start();
    }


    @Override
    public void execute() {
    }

    @Override
    public boolean isFinished() {
        if (Math.abs(ramp.getRampPos() - encoderValue.getAsDouble()) < GameConstants.RAMP_POS_THRESHOLD) {
            return true;
        }
        if (timer.hasElapsed(GameConstants.RAMP_POS_TIMEOUT)) {
            timeoutCounter.increaseTimeoutCount();
            timer.stop();
            return true;
        }
        return false;
    }
}
