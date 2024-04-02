package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.TimeoutCounter;

import java.util.function.DoubleSupplier;

public class RampMoveAndWait extends Command{

    private final Ramp ramp;
    private final double targetValue;
    private final Timer timer = new Timer();
    private final TimeoutCounter timeoutCounter;
    private boolean movingUp;

    public RampMoveAndWait(Ramp ramp, LightStrip lightStrip, DoubleSupplier encoderValueSupplier) {
        this.ramp = ramp;
        this.timeoutCounter = new TimeoutCounter("Wait Ramp", lightStrip);
        this.targetValue = encoderValueSupplier.getAsDouble();
        addRequirements(this.ramp);

    }

    @Override
    public void initialize() {
        double difference = ramp.getRampPos() - targetValue;
        if (difference >= 0) {
            // Needs to move down
            movingUp = false;
        } else {
            // Needs to move up
            movingUp = true;
        }
        ramp.setRampPos(targetValue);
        timer.reset();
        timer.start();
    }


    @Override
    public void execute() {
        ramp.updateFF();
    }

    @Override
    public boolean isFinished() {

        if ((movingUp && ramp.getForwardSwitchState()) ||
            (!movingUp && ramp.getReversedSwitchState())) {
            return true;
        }

        if ((Math.abs(ramp.getRampPos() - targetValue) < GameConstants.RAMP_POS_THRESHOLD)) {
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