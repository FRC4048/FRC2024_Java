package frc.robot.utils.logging;

import edu.wpi.first.wpilibj2.command.Command;

public class SingleLoggingCommand extends LoggingCommand{

    public SingleLoggingCommand(Command underlying) {
        this(null, underlying);
    }

    public SingleLoggingCommand(String namePrefix, Command underlying) {
        super(namePrefix, underlying.getName(), underlying);
    }

    @Override
    public String toString() {
        return getFullyQualifiedName();
    }
}
