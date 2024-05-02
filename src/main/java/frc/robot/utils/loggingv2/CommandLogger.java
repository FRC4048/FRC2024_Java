package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.littletonrobotics.junction.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CommandLogger {
    private final Map<Command, Queue<Boolean>> toLogCommandStatus = new HashMap<>();
    private static final CommandLogger inst = new CommandLogger();
    private boolean hasInit;

    public static CommandLogger get() {
        return inst;
    }

    private CommandLogger() {}

    public void init() {
        if (hasInit) return;
        CommandScheduler.getInstance().onCommandInitialize(command -> {
            Queue<Boolean> toLogBools = toLogCommandStatus.getOrDefault(command, new LinkedList<>());
            toLogBools.add(true);
            toLogCommandStatus.put(command, toLogBools);
        });
        CommandScheduler.getInstance().onCommandFinish(command -> {
            Queue<Boolean> toLogBools = toLogCommandStatus.getOrDefault(command, new LinkedList<>());
            toLogBools.add(false);
            toLogCommandStatus.put(command, toLogBools);
        });
        hasInit = true;
    }

    public void log() {
        toLogCommandStatus.forEach((command, isRunning) -> {
            Boolean poll = isRunning.poll();
            if (poll != null) {
                Logger.recordOutput("Command/" + command.toString(), poll);
            }
        });
    }
}
