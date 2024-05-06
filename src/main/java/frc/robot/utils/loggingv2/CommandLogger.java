package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import org.littletonrobotics.junction.Logger;

import java.util.*;

public class CommandLogger {
    private final LinkedHashMap<Command, Queue<Boolean>> toLogCommandStatus = new LinkedHashMap<>();
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
        Map.Entry<Command, Queue<Boolean>> entry = toLogCommandStatus.pollFirstEntry();
        List<Map.Entry<Command,Queue<Boolean>>> entriesToAdd = new ArrayList<>();
        while (entry != null){
            Boolean poll = entry.getValue().poll();
            if (poll != null) {
                Logger.recordOutput("Command/" + entry.getKey().toString(), poll);
            }
            if (entry.getValue().peek() != null){
                entriesToAdd.add(entry);
            }
            entry = toLogCommandStatus.pollFirstEntry();
        }
        entriesToAdd.forEach(e -> toLogCommandStatus.put(e.getKey(), e.getValue()));
    }
}
