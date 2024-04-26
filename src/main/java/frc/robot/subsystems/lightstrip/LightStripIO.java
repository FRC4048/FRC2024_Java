package frc.robot.subsystems.lightstrip;

import frc.robot.utils.BlinkinPattern;

public interface LightStripIO {
    void setPattern(BlinkinPattern pattern);
    void updateInputs(LightStripInputs inputs);
}
