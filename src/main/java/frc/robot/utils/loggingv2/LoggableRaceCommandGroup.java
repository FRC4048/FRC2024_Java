package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

import java.util.Arrays;

public class LoggableRaceCommandGroup extends ParallelRaceGroup implements Loggable {
    private String basicName = getClass().getSimpleName();
    private Command parent = new BlankCommand();

    @SafeVarargs
    public <T extends Command & Loggable>LoggableRaceCommandGroup(T... commands) {
        Arrays.stream(commands).forEach(c -> c.setParent(this));
        addCommands(commands);
    }

    @Override
    public String getBasicName(){
        return basicName;
    }

    @Override
    public String toString() {
        String prefix = parent.getName();
        if (!prefix.isBlank()){
            prefix += "/";
        }
        return prefix + getBasicName();
    }

    @Override
    public void setParent(Command loggable) {
        this.parent = loggable == null ? new BlankCommand() : loggable;
    }

    public LoggableRaceCommandGroup withBasicName(String name){
        this.basicName = name;
        return this;
    }
}
