package frc.robot.subsystems.feeder;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class FeederInputs implements LoggableInputs {

    public double feederSpeed = 0 ;
    public boolean isFwdTripped = false;
    public boolean areBeamsEnabled = false;

    @Override
    public void toLog(LogTable table) {
        table.put("feederSpeed", feederSpeed);
        table.put("isFwdTripped", isFwdTripped);
        table.put("areBeamsEnabled", areBeamsEnabled);
    }

    @Override
    public void fromLog(LogTable table) {
        feederSpeed = table.get("feederSpeed", feederSpeed);
        isFwdTripped = table.get("isFwdTripped", isFwdTripped);
        areBeamsEnabled = table.get("areBeamsEnabled", areBeamsEnabled);
    }
}
