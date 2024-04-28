package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

import java.util.Arrays;

public class LoggableParallelCommandGroup extends ParallelCommandGroup implements Loggable {
    private String basicName = getClass().getName();
    private String parentName;

    public LoggableParallelCommandGroup(Loggable... commands) {
        Arrays.stream(commands).forEach(c -> c.setParent(this));
        addCommands(Arrays.stream(commands).map(l -> (Command) l).toList().toArray(Command[]::new));
    }

    @Override
    public String getBasicName(){
        return basicName;
    }

    @Override
    public String getName() {
        return parentName + getBasicName() + "_" + Integer.toHexString(hashCode());
    }

    @Override
    public void setParent(Loggable loggable) {
        parentName = loggable.getBasicName();
    }

    public Command withBasicName(String name){
        this.basicName = name;
        return this;
    }
}
