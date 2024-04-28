package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.Arrays;

public class LoggableSequentialCommandGroup extends SequentialCommandGroup implements Loggable {
    private String basicName = getClass().getName();
    private Command parent = new BlankCommand();

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
        return parent.getName() + getBasicName() + "_" + Integer.toHexString(hashCode());
    }

    @Override
    public void setParent(Command loggable) {
        this.parent = loggable;
    }

    public LoggableSequentialCommandGroup withBasicName(String name){
        this.basicName = name;
        return this;
    }
}
