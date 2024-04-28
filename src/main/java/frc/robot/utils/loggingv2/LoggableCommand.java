package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;

public class LoggableCommand extends Command implements Loggable {
    private String basicName = getClass().getName();
    private Command parent = new BlankCommand();

    @Override
    public String getBasicName() {
        return basicName;
    }

    @Override
    public String getName() {
        return parent.getName() + getBasicName() + "_" + Integer.toHexString(hashCode());
    }

    @Override
    public void setParent(Command loggable) {
        this.parent = loggable;
    }

    public LoggableCommand withBasicName(String name){
        basicName = name;
        return this;
    }
}
