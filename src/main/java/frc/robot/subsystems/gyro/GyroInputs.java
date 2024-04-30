package frc.robot.subsystems.gyro;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class GyroInputs implements LoggableInputs {
    public double[] anglesInDeg = new double[0];
    public double[] anglesTimeStamps = new double[0];
    public double angleOffset = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("anglesInDeg", anglesInDeg);
        table.put("anglesTimeStamps", anglesTimeStamps);
        table.put("angleOffset", angleOffset);
    }

    @Override
    public void fromLog(LogTable table) {
        anglesInDeg = table.get("anglesInDeg", anglesInDeg);
        anglesTimeStamps = table.get("anglesTimeStamps", anglesTimeStamps);
        angleOffset = table.get("angleOffset", angleOffset);
    }
}
