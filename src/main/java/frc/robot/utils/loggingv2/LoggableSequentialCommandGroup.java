package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.Arrays;

public class LoggableSequentialCommandGroup extends SequentialCommandGroup implements Loggable {
    private String basicName = getClass().getName();
    private String parentName;

    public <T extends Command & Loggable>LoggableSequentialCommandGroup(T... commands) {
        Arrays.stream(commands).forEach(c -> c.setParent(this));
        addCommands(commands);
    }

    @Override
    public String getBasicName(){
        return getClass().getName();
    }

    @Override
    public String getName() {
        return parentName + getBasicName() + "_" + Integer.toHexString(hashCode());
    }

    @Override
    public void setParent(Loggable loggable) {
        this.parentName = loggable.getBasicName();
    }

    public LoggableSequentialCommandGroup withBasicName(String name){
        this.basicName = name;
        return this;
    }
}
