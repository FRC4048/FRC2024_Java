package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;

import java.util.Arrays;

public class LoggableDeadlineCommandGroup extends ParallelDeadlineGroup implements Loggable {
    private String basicName = getClass().getSimpleName();
    private Command parent = new BlankCommand();

    @SafeVarargs
    public <T extends Command & Loggable> LoggableDeadlineCommandGroup(T deadline, T... others) {
        super(new Command() {});
        Arrays.stream(others).forEach(c -> c.setParent(this));
        deadline.setParent(this);
        setDeadline(deadline);
        addCommands(others);
    }

    @Override
    public String getBasicName() {
        return basicName;
    }

    @Override
    public String toString() {
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

    public LoggableDeadlineCommandGroup withBasicName(String name) {
        basicName = name;
        return this;
    }
}
