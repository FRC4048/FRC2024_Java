package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;

public class LoggableCommand extends Command implements Loggable {
    private String parentName;

    @Override
    public String getBasicName() {
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
}
