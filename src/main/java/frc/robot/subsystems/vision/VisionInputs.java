package frc.robot.subsystems.vision;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class VisionInputs implements LoggableInputs {
    public double tv;
    public double tx;
    public double ty;

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
