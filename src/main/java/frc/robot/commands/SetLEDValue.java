package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LightStrip;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class SetLEDValue extends Command {

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
