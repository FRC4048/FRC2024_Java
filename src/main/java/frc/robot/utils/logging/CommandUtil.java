package frc.robot.utils.logging;

import edu.wpi.first.wpilibj2.command.*;

/**
 * Utility class for handling logged commands. The class follows the same pattern of WPILib's {@link Commands} class.
 * Logging commands generate log messages using {@link edu.wpi.first.util.datalog.DataLog}. Each command
 * logs its {@link Command#initialize()} and {@link Command#end(boolean)}.
 * The logs are meant to be viewed in the AdvantageScope UI. When a command is started it shows a value of {@code TRUE}
 * in the timeline and when it is done is shows a value of {@code FALSE}.
 * Commands are also showing up as hierarchical nested groups in the tool, where {@code CommandGroup} (e.g. {@link SequentialCommandGroup})
 * shows up as a container in the UI for its child commands.
 * <p/>
 * Usage:
 * <p/>
 * When creating your own command (any regular command), use the {@link #logged(Command)} to make that command loggable.
 * Without any other modification, this command will show up with its given name in the UI.
 * For example:
 * <pre>
 *     Command loggedCommand = CommandUtil.logged(new MyCommand());
 * </pre>
 * <p/>
 * When you need to create a container (e.g. a {@link SequentialCommandGroup}) you can use the {@link #sequence(String, Command...)} method
 * to create a sequential, loggable command group and make all of its child commands loggable as well.
 * For example:
 * <pre>
 *     Command pickUp = CommandUtil.sequence("ArmSequence", new OpenGripper(), new RaiseArm(), new CloseGripper());
 * </pre>
 *
 * That also works for nesting command groups within command groups:
 * <pre>
 *     Command pickUp = CommandUtil.sequence("ArmSequence", new OpenGripper(), new RaiseArm(), new CloseGripper());
 *     Command pickupAndDrive = CommandUtil.parallel("WalkWhileChewingGum", pickup, new Drive());
 * </pre>
 *
 * and also for nesting command groups that are custom-created:
 * (Note the "extends SequentialLoggingCommandGroup" - DO NOT extend regular SequentialCommandGroup or this will not work)
 * <pre>
 *     class PickupGroup extends SequentialLoggingCommandGroup {
 *         public PickupGroup() {
 *             super("Pickup", new OpenGripper(), new RaiseArm(), new CloseGripper());
 *         }
 *     }
 *     Command pickUp = new PickupGroup();
 *     Command pickupAndDrive = CommandUtil.parallel("WalkWhileChewingGum", pickup, new Drive());
 * </pre>
 */
public class CommandUtil {
    public static final String COMMAND_PREFIX = "Commands";
    public static final String NAME_SEPARATOR = "/";

    private CommandUtil() {
    }

    /**
     * Make a logging command from the given command
     * @param command the command to wrap
     * @return a {@link LoggingCommand} that wraps the given command
     */
    public static Command logged(Command command) {
        return new SingleLoggingCommand(command);
    }

    /**
     * Make a logging command from the given command, with a custom name hierarchy.
     * The logging command will be placed within the given name prefix naming hierarchy instead of at the root.
     * @param namePrefix the nesting hierarchy prefix for the command. If null the command is going to log at the
     *                   root level. If not null it will be nested in this namespace.
     * @param command the command to wrap
     * @return a {@link LoggingCommand} that wraps the given command
     */
    public static Command logged(String namePrefix, Command command) {
        return new SingleLoggingCommand(namePrefix, command);
    }

    /**
     * Return a logged command that runs once and stops (see {@link edu.wpi.first.wpilibj2.command.Commands#runOnce(Runnable, Subsystem...)}.
     * This will return a loggable anonymous command with the given name.
     * @param name the name of the resulting command
     * @param action the action to perform (only once)
     * @param requirements the subsystem requirements for the command
     * @return a {@link LoggingCommand} that wraps the given action
     */
    public static Command runOnce(String name, Runnable action, Subsystem... requirements) {
        Command command = Commands.runOnce(action, requirements);
        command.setName(name);
        return logged(command);
    }

    /**
     * Return a loggable {@link SequentialCommandGroup} that executes the given commands sequentially. The given commands
     * can be any command or command group - they will be converted to loggable commands implicitly.
     * @param sequenceName the name of the resulting command group
     * @param commands the commands to put in the group
     * @return the created loggable command group
     */
    public static Command sequence(String sequenceName, Command... commands) {
        return new SequentialLoggingCommand(sequenceName, commands);
    }

    /**
     * Return a loggable {@link ParallelCommandGroup} that executes the given commands in parallel. The given commands
     * can be any command or command group - they will be converted to loggable commands implicitly.
     * @param sequenceName the name of the resulting command group
     * @param commands the commands to put in the group
     * @return the created loggable command group
     */
    public static Command parallel(String sequenceName, Command... commands) {
        return new ParallelLoggingCommand(sequenceName, commands);
    }

    public static Command race(String sequenceName, Command... commands) {
        return new RaceLoggingCommand(sequenceName, commands);
    }

    public static LoggingCommand[] wrapForLogging(String prefix, Command... commands) {
        // Do not use streams due to efficiency
        LoggingCommand[] newCommands = new LoggingCommand[commands.length];
        for (int i = 0; i < commands.length; i++) {
            newCommands[i] = wrapForLogging(prefix, commands[i]);
        }
        return newCommands;
    }

    private static LoggingCommand wrapForLogging(String prefix, Command command) {
        if (command instanceof LoggingCommand loggingCommand) {
            loggingCommand.appendNamePrefix(prefix);
            return loggingCommand;
        } else {
            // New command located in the given sequence root
            return new SingleLoggingCommand(prefix, command);
        }
    }
}
