package frc.robot.utils.logging;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

public class RaceLoggingCommand extends LoggingCommand {
    private static final String THIS_NAME = "-this";

    private LoggingCommand[] loggingCommands;

    public RaceLoggingCommand(String namePrefix, LoggingCommand... commands) {
        super(namePrefix, THIS_NAME, new ParallelRaceGroup(commands));
        this.loggingCommands = commands;
    }

    @Override
    public void setName(String name) {
        // Do not change the logging name for this command (it is fixed)
        getUnderlying().setName(name);
    }

    @Override
    public void setNamePrefix(String prefix) {
        super.setNamePrefix(prefix);
        setChildrenPrefix(prefix);
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }

    private void setChildrenPrefix(String prefix) {
        // Recursively change the prefix for all child commands
        for (LoggingCommand loggingCommand : loggingCommands) {
            loggingCommand.setNamePrefix(prefix);
        }
    }
}
