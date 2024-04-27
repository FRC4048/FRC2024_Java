package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

import java.util.Arrays;

public class LoggableRaceCommandGroup extends ParallelRaceGroup implements Loggable {
    private String parentName;

    public LoggableRaceCommandGroup(Loggable... commands) {
        Arrays.stream(commands).forEach(c -> c.setParent(this));
        addCommands(Arrays.stream(commands).map(l -> (Command) l).toList().toArray(Command[]::new));
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
    public void setParent(Loggable loggable) {
        parentName = loggable.getBasicName();
    }
}
