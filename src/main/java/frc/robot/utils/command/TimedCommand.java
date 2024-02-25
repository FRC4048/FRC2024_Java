package frc.robot.utils.command;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;

public class TimedCommand extends Command {
    private final double timeout;
    private final Timer timer;

    public TimedCommand(double timeout) {
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
        return timer.hasElapsed(timeout);
    }
}
