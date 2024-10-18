package frc.robot.subsystems;

import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface LoggableIO<T extends LoggableInputs> {
    void updateInputs(T inputs);
}
