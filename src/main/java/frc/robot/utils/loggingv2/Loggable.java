package frc.robot.utils.loggingv2;

import edu.wpi.first.wpilibj2.command.Command;

public interface Loggable {
    String getBasicName();
    void setParent(Command loggable);
}