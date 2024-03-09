package frc.robot.utils.logging;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

import java.util.Arrays;

public class ParallelDeadlineLoggingCommand extends GroupLoggingCommand {
    private LoggingCommand deadlineLoggingCommand;

    /**
     * Constructor for parallel deadline command group.
     *
     * @param namePrefix the name for the group - this is where the sub-commands for this group will be nested in
     * @param deadline   the deadline command - the one that when ends, stops other commands
     * @param commands   the sub commands for this group (either regular commands or LoggingCommand are OK)
     */
    public ParallelDeadlineLoggingCommand(String namePrefix, Command deadline, Command... commands) {
        // Since we can't initialize the deadlineGroup with empty commands, use the special constructor to pass in the
        // underlying afterward
        super(namePrefix, "(Deadline)");

        deadlineLoggingCommand = CommandUtil.wrapForLogging(namePrefix, deadline)[0];
        LoggingCommand[] wrapped = CommandUtil.wrapForLogging(namePrefix, commands);
        setUnderlying(new ParallelDeadlineGroup(deadlineLoggingCommand, wrapped));
        addLoggingCommands(Arrays.asList(wrapped));
    }

    public final void addCommands(Command... commands) {
        LoggingCommand[] wrapped = CommandUtil.wrapForLogging(getNamePrefix(), commands);
        ((ParallelDeadlineGroup) getUnderlying()).addCommands(wrapped);
        addLoggingCommands(Arrays.asList(wrapped));
    }

    @Override
    public void appendNamePrefix(String prefix) {
        super.appendNamePrefix(prefix);
        // Change the name for the deadline
        deadlineLoggingCommand.appendNamePrefix(prefix);
    }
}
