package frc.robot.utils.logging;

import edu.wpi.first.wpilibj2.command.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for logged grouped commands (parallel, race, sequential...)
 */
public abstract class GroupLoggingCommand extends LoggingCommand {
    // A copy of the children (since we can't access them from the regular groups)
    private final List<LoggingCommand> childLoggingCommands;

    /**
     * Constructor for logged group command.
     *
     * @param namePrefix the prefix for the name (comes in front of hte group name)
     * @param underlying the group command that is wrapped by this command
     */
    public GroupLoggingCommand(String namePrefix, String commandName, Command underlying) {
        super(namePrefix, commandName, underlying);
        childLoggingCommands = new ArrayList<>();
    }

    /**
     * A special constructor to allow for the creation of the command when we can't create the underlying up front.
     * It is assumed that the {@link #setUnderlying(Command)} method is called immediately following the construction.
     */
    protected GroupLoggingCommand(String namePrefix, String commandName) {
        super(namePrefix, commandName);
        childLoggingCommands = new ArrayList<>();
    }

    public final void addLoggingCommands(List<LoggingCommand> commands) {
        childLoggingCommands.addAll(commands);
    }

    @Override
    public void setName(String name) {
        // Do not change the logging name for this command (it is fixed)
        getUnderlying().setName(name);
    }

    @Override
    public void appendNamePrefix(String prefix) {
        // Change the name for this command
        super.appendNamePrefix(prefix);
        // Change the name for the children
        appendChildrenPrefix(prefix);
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }

    // For testing
    public List<LoggingCommand> getChildLoggingCommands() {
        return childLoggingCommands;
    }

    private void appendChildrenPrefix(String prefix) {
        // Recursively change the prefix for all child commands
        for (LoggingCommand loggingCommand : childLoggingCommands) {
            loggingCommand.appendNamePrefix(prefix);
        }
    }
}
