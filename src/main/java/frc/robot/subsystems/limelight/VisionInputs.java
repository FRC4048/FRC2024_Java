package frc.robot.subsystems.limelight;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class VisionInputs implements LoggableInputs {
    public double tv = 0;
    public double tx = 0;
    public double ty = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("tv", tv);
        table.put("tx", tx);
        table.put("ty", ty);
    }

    @Override
    public void fromLog(LogTable table) {
        tv = table.get("tv", tv);
        tx = table.get("tx", tx);
        ty = table.get("ty", ty);
    }
}
