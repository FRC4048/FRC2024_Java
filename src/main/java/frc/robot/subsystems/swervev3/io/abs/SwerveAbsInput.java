package frc.robot.subsystems.swervev3.io.abs;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class SwerveAbsInput implements LoggableInputs {

    public double absEncoderPosition = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("absEncoderPosition", absEncoderPosition);
    }

    @Override
    public void fromLog(LogTable table) {
        absEncoderPosition = table.get("absEncoderPosition", absEncoderPosition);
    }
}
