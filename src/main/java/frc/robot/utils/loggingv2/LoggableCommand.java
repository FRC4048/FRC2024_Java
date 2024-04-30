package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;

public class LoggableCommand extends Command implements Loggable {
    private String basicName = getClass().getSimpleName();
    private Command parent = new BlankCommand();

    @Override
    public String getBasicName() {
        return basicName;
    }

    @Override
    public String getName() {
        String prefix = parent.getName();
        if (!prefix.isBlank()){
            prefix += "/";
        }
        return prefix + getBasicName();
    }

    @Override
    public void setParent(Command loggable) {
        this.parent = loggable == null ? new BlankCommand() : loggable;
    }

    public LoggableCommand withBasicName(String name) {
        basicName = name;
        return this;
    }
}
