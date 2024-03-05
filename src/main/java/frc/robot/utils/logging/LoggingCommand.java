package frc.robot.utils.logging;

import edu.wpi.first.util.datalog.BooleanLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.constants.Constants;

import java.util.Set;

public abstract class LoggingCommand extends Command {
    private String namePrefix;
    private String commandName;
    private Command underlying;

    private String fullyQualifiedName;
    // Lazy initialized log entry to make sure we don't create it until it is needed (and command is scheduled)
    // otherwise we get an empty line in the log visualization tool
    private BooleanLogEntry logEntry;

    public LoggingCommand(String namePrefix, String commandName, Command underlying) {
        this.namePrefix = namePrefix;
        this.commandName = commandName;
        this.underlying = underlying;
        resetLoggingName(namePrefix, commandName);
    }

    /**
     * A special constructor to allow for the creation of the command when we can't create the underlying up front.
     * It is assumed that the {@link #setUnderlying(Command)} method is called immediately following the construction.
     */
    protected LoggingCommand(String namePrefix, String commandName) {
        this.namePrefix = namePrefix;
        this.commandName = commandName;
        resetLoggingName(namePrefix, commandName);
    }

    /**
     * Second phase of initialization - set the underlying command
     */
    protected void setUnderlying(Command underlying) {
        this.underlying = underlying;
    }

    @Override
    public void initialize() {
        if (Constants.ENABLE_LOGGING) getLoggingEntry().append(true);
        underlying.initialize();
    }

    @Override
    public void execute() {
        underlying.execute();
    }

    @Override
    public void end(boolean interrupted) {
        if (Constants.ENABLE_LOGGING) underlying.end(interrupted);
        getLoggingEntry().append(false);
    }

    @Override
    public boolean isFinished() {
        return underlying.isFinished();
    }

    @Override
    public Set<Subsystem> getRequirements() {
        return underlying.getRequirements();
    }

    @Override
    public String getName() {
        return underlying.getName();
    }

    @Override
    public void setName(String name) {
        underlying.setName(name);
        this.commandName = name;
        resetLoggingName(namePrefix, name);
    }

    @Override
    public String getSubsystem() {
        return underlying.getSubsystem();
    }

    @Override
    public void setSubsystem(String subsystem) {
        underlying.setSubsystem(subsystem);
    }

    @Override
    public boolean runsWhenDisabled() {
        return underlying.runsWhenDisabled();
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String prefix) {
        this.namePrefix = prefix;
        resetLoggingName(namePrefix, commandName);
    }

    public Command getUnderlying() {
        return underlying;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    private void resetLoggingName(String namePrefix, String name) {
        fullyQualifiedName = CommandUtil.COMMAND_PREFIX + CommandUtil.NAME_SEPARATOR +
                ((namePrefix == null) ? name : namePrefix + CommandUtil.NAME_SEPARATOR + name);
        if (logEntry != null) {
            logEntry.finish();
            logEntry = null;
        }
    }

    private BooleanLogEntry getLoggingEntry() {
        if (logEntry == null) {
            logEntry = new BooleanLogEntry(DataLogManager.getLog(), fullyQualifiedName);
        }
        return logEntry;
    }
}
