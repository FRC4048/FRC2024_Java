package frc.robot.subsystems.colorsensor;

import edu.wpi.first.wpilibj.util.Color;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ColorSensorInputs implements LoggableInputs {
    public Color rawColor;
    @Override
    public void toLog(LogTable table) {
        table.put("color", rawColor.toHexString());
    }

    @Override
    public void fromLog(LogTable table) {
        rawColor = new Color(table.get("color", rawColor.toHexString()));
    }
}
