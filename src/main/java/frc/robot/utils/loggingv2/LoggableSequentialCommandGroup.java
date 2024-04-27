package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class LoggableSequentialCommandGroup extends SequentialCommandGroup implements Loggable {
    private final String parentName;

    public LoggableSequentialCommandGroup(Loggable parent, Command... commands) {
        addCommands(commands);
        this.parentName = parent.getLoggableName();
    }
    public LoggableSequentialCommandGroup(Command... commands) {
        addCommands(commands);
        this.parentName = "";
    }

    @Override
    public String getLoggableName(){
        return parentName + getName() + "_" + Integer.toHexString(hashCode());
    }
}
