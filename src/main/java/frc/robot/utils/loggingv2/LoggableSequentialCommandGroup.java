package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.Arrays;

public class LoggableSequentialCommandGroup extends SequentialCommandGroup implements Loggable {
    private String basicName = getClass().getSimpleName();
    private Command parent = new BlankCommand();

    public <T extends Command & Loggable>LoggableSequentialCommandGroup(T... commands) {
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

    public LoggableSequentialCommandGroup withBasicName(String name){
        this.basicName = name;
        return this;
    }
}
