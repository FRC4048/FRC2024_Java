package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

import java.util.Arrays;

public class LoggableDeadlineCommandGroup extends ParallelDeadlineGroup implements Loggable {
    private String parentName;

    public LoggableDeadlineCommandGroup(Loggable deadline, Loggable... others) {
        super(new Command(){});
        Arrays.stream(others).forEach(c -> c.setParent(this));
        deadline.setParent(this);
        setDeadline(deadline.asCommand());
        addCommands(Arrays.stream(others).map(Loggable::asCommand).toList().toArray(Command[]::new));
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
    public Command asCommand() {
        return this;
    }

    @Override
    public void setParent(Loggable loggable) {
        parentName = loggable.getBasicName();
    }
}
