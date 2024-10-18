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
        CommandScheduler.getInstance().onCommandInterrupt(command -> {
            Queue<Boolean> toLogBools = toLogCommandStatus.getOrDefault(command, new LinkedList<>());
            toLogBools.add(false);
            toLogCommandStatus.put(command, toLogBools);
        });
        hasInit = true;
    }

    public void log() {
        Iterator<Map.Entry<Command, Queue<Boolean>>> iterator = toLogCommandStatus.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Command, Queue<Boolean>> entry = iterator.next();
            Boolean poll = entry.getValue().poll();
            if (poll != null) {
                Logger.recordOutput("Command/" + entry.getKey().toString(), poll);
            }
            if (entry.getValue().peek() == null){
                iterator.remove();
            }
        }
    }
}
