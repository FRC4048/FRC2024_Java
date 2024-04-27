package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class LoggableParallelCommandGroup extends ParallelCommandGroup implements Loggable {
    private final String parentName;

    public LoggableParallelCommandGroup(Command... commands) {
        addCommands(commands);
        this.parentName = "";
    }

    public LoggableParallelCommandGroup(Loggable parent, Command... commands) {
        addCommands(commands);
        this.parentName = parent.getLoggableName();
    }

    @Override
    public String getLoggableName(){
        return parentName + getName() + "_" + Integer.toHexString(hashCode());
    }
}
