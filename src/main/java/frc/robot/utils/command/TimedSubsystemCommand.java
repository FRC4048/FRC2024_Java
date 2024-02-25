package frc.robot.utils.command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TimedSubsystemCommand<T extends SubsystemBase> extends TimedCommand implements SubsystemCommand<T> {
    private final T subsystem;

    public TimedSubsystemCommand(T subsystem, double timeout) {
        super(timeout);
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public T getSystem() {
        return subsystem;
    }
}
