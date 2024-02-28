package frc.robot.utils.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.TimeoutCounter;

public class TimedCommand extends Command {
    private final TimeoutCounter counter;
    private final double timeout;
    private final Timer timer;

    public TimedCommand(double timeout) {
        this.counter = new TimeoutCounter(getClass().getSimpleName());
        this.timeout = timeout;
        this.timer = new Timer();
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public boolean isFinished() {
        if (timer.hasElapsed(timeout)){
            counter.increaseTimeoutCount();
            return true;
        }
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }
}
