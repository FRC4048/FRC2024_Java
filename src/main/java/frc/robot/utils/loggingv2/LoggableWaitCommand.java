package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.WaitCommand;

public class LoggableWaitCommand extends WaitCommand implements Loggable {
    private final String parentName;

    public LoggableWaitCommand(double seconds) {
        super(seconds);
        this.parentName = "";
    }

    public LoggableWaitCommand(Loggable parent, double seconds) {
        super(seconds);
        this.parentName = parent.getLoggableName();
    }

    @Override
    public String getLoggableName() {
        return parentName + getName() + "_" + Integer.toHexString(hashCode());
    }
}
