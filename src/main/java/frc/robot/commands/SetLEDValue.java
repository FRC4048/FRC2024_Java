package frc.robot.commands;

import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.loggingv2.LoggableCommand;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class SetLEDValue extends LoggableCommand {

    LightStrip lightStrip;
    
    public SetLEDValue(LightStrip lightStrip) {
        this.lightStrip = lightStrip;
    }

    @Override
    public void initialize() {
        double targetValue = SmartShuffleboard.getDouble("LED", "TargetValue", 0.0);
        this.lightStrip.setPattern(BlinkinPattern.of(targetValue));
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
