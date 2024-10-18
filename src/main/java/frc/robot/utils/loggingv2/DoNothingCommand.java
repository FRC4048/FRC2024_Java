package frc.robot.utils.loggingv2;

public class DoNothingCommand extends LoggableCommand {
    @Override
    public boolean isFinished() {
        return true;
    }

}
