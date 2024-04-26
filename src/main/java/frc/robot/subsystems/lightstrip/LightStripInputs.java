package frc.robot.subsystems.lightstrip;

import frc.robot.utils.BlinkinPattern;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class LightStripInputs implements LoggableInputs {
    public BlinkinPattern pattern = BlinkinPattern.BLACK;

    @Override
    public void toLog(LogTable table) {
        table.put("patternPWM", pattern.getPwm());
    }

    @Override
    public void fromLog(LogTable table) {
        pattern = BlinkinPattern.of(table.get("patternPWM", pattern.getPwm()));
    }
}
