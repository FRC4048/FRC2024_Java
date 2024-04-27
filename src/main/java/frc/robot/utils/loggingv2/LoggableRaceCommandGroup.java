package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

public class LoggableRaceCommandGroup extends ParallelRaceGroup implements Loggable {
    private final String parentName;

    public LoggableRaceCommandGroup(Loggable parent, Command... commands) {
        addCommands(commands);
        this.parentName = parent.getLoggableName();
    }
    public LoggableRaceCommandGroup(Command... commands) {
        addCommands(commands);
        this.parentName = "";
    }

    @Override
    public String getLoggableName(){
        return parentName + getName() + "_" + Integer.toHexString(hashCode());
    }
}
