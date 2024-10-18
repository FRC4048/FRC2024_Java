package frc.robot.commands;

import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.loggingv2.LoggableCommand;

public class SetLedPattern extends LoggableCommand {
    private final LightStrip lightStrip;
    private final BlinkinPattern pattern;

    public SetLedPattern(LightStrip lightStrip, BlinkinPattern pattern) {
        this.lightStrip = lightStrip;
        this.pattern = pattern;
    }

    @Override
    public void initialize() {
        lightStrip.setPattern(pattern);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
