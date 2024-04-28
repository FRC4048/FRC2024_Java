package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class LoggableWaitCommand extends WaitCommand implements Loggable {
    private String basicName = getClass().getName();
    private Command parent = new BlankCommand();


    public LoggableWaitCommand(double seconds) {
        super(seconds);
    }

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
        parent = loggable;
    }

    public LoggableWaitCommand withBasicName(String name){
        this.basicName = name;
        return this;
    }
}
