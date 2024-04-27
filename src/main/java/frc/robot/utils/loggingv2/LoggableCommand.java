package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;

public class LoggableCommand extends Command implements Loggable {
    private final String parentName;

    public LoggableCommand() {
        this.parentName = "";
    }

    public LoggableCommand(Loggable parent) {
        this.parentName = parent.getLoggableName();
    }

    @Override
    public String getLoggableName() {
        return parentName + getName() + "_" + Integer.toHexString(hashCode());
    }
}
