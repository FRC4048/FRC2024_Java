package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import java.util.Arrays;

public class LoggableSequentialCommandGroup extends SequentialCommandGroup implements Loggable {
    private String parentName;

    public LoggableSequentialCommandGroup(Loggable... commands) {
        Arrays.stream(commands).forEach(c -> c.setParent(this));
        addCommands(Arrays.stream(commands).map(Loggable::asCommand).toList().toArray(Command[]::new));
        this.parentName = "";
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
        this.parentName = loggable.getBasicName();
    }
}
