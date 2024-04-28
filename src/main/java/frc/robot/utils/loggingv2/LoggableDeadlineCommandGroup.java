package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

import java.util.Arrays;

public class LoggableDeadlineCommandGroup extends ParallelDeadlineGroup implements Loggable {
    private String basicName = getClass().getName();
    private String parentName;

    @SafeVarargs
    public <T extends Command & Loggable>LoggableDeadlineCommandGroup(T deadline, T... others) {
        super(new Command(){});
        Arrays.stream(others).forEach(c -> c.setParent(this));
        deadline.setParent(this);
        setDeadline(deadline);
        addCommands(others);
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
        basicName = name;
        return this;
    }
}
