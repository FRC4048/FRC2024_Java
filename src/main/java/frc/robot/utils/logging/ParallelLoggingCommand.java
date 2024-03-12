package frc.robot.utils.logging;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import java.util.Arrays;

public class ParallelLoggingCommand extends GroupLoggingCommand {
    /**
     * Constructor for parallel command group.
     *
     * @param namePrefix the name for the group - this is where the sub-commands for this group will be nested in
     * @param commands   the sub commands for this group (either regular commands or LoggingCommand are OK)
     */
    public ParallelLoggingCommand(String namePrefix, Command... commands) {
        // Call super with an empty group, populate children afterward
        super(namePrefix, "(Parallel)", new ParallelCommandGroup());

        LoggingCommand[] wrapped = CommandUtil.wrapForLogging(namePrefix, commands);
        ((ParallelCommandGroup) getUnderlying()).addCommands(wrapped);
        addLoggingCommands(Arrays.asList(wrapped));
    }

    public final void addCommands(Command... commands) {
        LoggingCommand[] wrapped = CommandUtil.wrapForLogging(getNamePrefix(), commands);
        ((ParallelCommandGroup) getUnderlying()).addCommands(wrapped);
        addLoggingCommands(Arrays.asList(wrapped));
    }
}
