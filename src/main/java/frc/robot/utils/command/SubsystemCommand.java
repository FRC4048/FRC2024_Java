package frc.robot.utils.command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public interface SubsystemCommand<T extends SubsystemBase> {
    T getSystem();
}
