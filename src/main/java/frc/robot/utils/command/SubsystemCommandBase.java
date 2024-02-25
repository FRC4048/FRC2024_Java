package frc.robot.utils.command;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SubsystemCommandBase<T extends SubsystemBase> extends Command implements SubsystemCommand<T> {
    private final T subsystem;

    protected SubsystemCommandBase(T subsystem) {
        this.subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public T getSystem() {
        return subsystem;
    }
}
