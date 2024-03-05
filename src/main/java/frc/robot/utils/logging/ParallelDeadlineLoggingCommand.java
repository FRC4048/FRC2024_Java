package frc.robot.utils.logging;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

import java.util.Arrays;
import java.util.List;

public class ParallelDeadlineLoggingCommand extends LoggingCommand {
    private static final String THIS_NAME = "-this";

    private LoggingCommand deadlineLoggingCommand;
    private List<LoggingCommand> loggingCommands;

    /**
     * Constructor for parallel deadline command group.
     * @param namePrefix the name for the group - this is where the sub-commands for this group will be nested in
     * @param deadline the deadline command - the one that when ends, stops other commands
     * @param commands the sub commands for this group (either regular commands or LoggingCommand are OK)
     */
    public ParallelDeadlineLoggingCommand(String namePrefix, Command deadline, Command... commands) {
        super(namePrefix, THIS_NAME);
        // Since we can't initialize the deadlineGroup with empty commands, use the special constructor to pass in the
        // underlying afterward
        deadlineLoggingCommand = CommandUtil.wrapForLogging(namePrefix, deadline)[0];
        LoggingCommand[] wrapped = CommandUtil.wrapForLogging(namePrefix, commands);
        this.loggingCommands = Arrays.asList(wrapped);
        setUnderlying(new ParallelDeadlineGroup(deadlineLoggingCommand, wrapped));
    }

    public final void addCommands(Command... commands) {
        LoggingCommand[] wrapped = CommandUtil.wrapForLogging(getNamePrefix(), commands);
        ((ParallelCommandGroup) getUnderlying()).addCommands(wrapped);
        loggingCommands.addAll(Arrays.asList(wrapped));
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
