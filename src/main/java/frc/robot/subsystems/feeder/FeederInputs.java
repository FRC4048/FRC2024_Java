package frc.robot.subsystems.feeder;

import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj.util.Color;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class FeederInputs implements LoggableInputs {

    public double feederSpeed;
    public ColorMatchResult colorMatchResult;
    public boolean isFwdTripped;
    public boolean areBeamsEnabled;

    @Override
    public void toLog(LogTable table) {
        table.put("feederSpeed", feederSpeed);
        table.put("colorMatchResultColor", colorMatchResult.color.toHexString());
        table.put("colorMatchResultConfidence", colorMatchResult.confidence);
        table.put("isFwdTripped", isFwdTripped);
        table.put("areBeamsEnabled", areBeamsEnabled);
    }

    @Override
    public void fromLog(LogTable table) {
        feederSpeed = table.get("feederSpeed", feederSpeed);
        colorMatchResult = new ColorMatchResult(
                new Color(table.get("colorMatchResultColor", colorMatchResult.color.toHexString())),
                table.get("colorMatchResultConfidence", colorMatchResult.confidence)
        );
        isFwdTripped = table.get("isFwdTripped", isFwdTripped);
        areBeamsEnabled = table.get("areBeamsEnabled", areBeamsEnabled);
    }
}
