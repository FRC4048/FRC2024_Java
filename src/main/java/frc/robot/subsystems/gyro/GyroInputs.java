package frc.robot.subsystems.gyro;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class GyroInputs implements LoggableInputs {
    public double anglesInDeg = 0;
    public double angleOffset = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("anglesInDeg", anglesInDeg);
        table.put("angleOffset", angleOffset);
    }

    @Override
    public void fromLog(LogTable table) {
        anglesInDeg = table.get("anglesInDeg", anglesInDeg);
        angleOffset = table.get("angleOffset", angleOffset);
    }
}