package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class LoggableWaitCommand extends WaitCommand implements Loggable {
    private String parentName;

    public LoggableWaitCommand(double seconds) {
        super(seconds);
    }

    @Override
    public String getBasicName() {
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
