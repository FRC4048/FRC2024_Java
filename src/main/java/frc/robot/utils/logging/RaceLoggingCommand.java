package frc.robot.utils.logging;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

import java.util.Arrays;

public class RaceLoggingCommand extends GroupLoggingCommand {
    /**
     * Constructor for race command group.
     *
     * @param namePrefix the name for the group - this is where the sub-commands for this group will be nested in
     * @param commands   the sub commands for this group (either regular commands or LoggingCommand are OK)
     */
    public RaceLoggingCommand(String namePrefix, Command... commands) {
        // Call super with an empty group, populate children afterward
        super(namePrefix, "(Race)", new ParallelRaceGroup());

        LoggingCommand[] wrapped = CommandUtil.wrapForLogging(namePrefix, commands);
        ((ParallelRaceGroup) getUnderlying()).addCommands(wrapped);
        addLoggingCommands(Arrays.asList(wrapped));
    }

    public final void addCommands(Command... commands) {
        LoggingCommand[] wrapped = CommandUtil.wrapForLogging(getNamePrefix(), commands);
        ((ParallelRaceGroup) getUnderlying()).addCommands(wrapped);
        addLoggingCommands(Arrays.asList(wrapped));
    }
}
