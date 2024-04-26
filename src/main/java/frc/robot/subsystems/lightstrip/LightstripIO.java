package frc.robot.subsystems.lightstrip;

import frc.robot.utils.BlinkinPattern;

public interface LightstripIO {
    void setPattern(BlinkinPattern pattern);
    void updateInputs(LightStripInputs inputs);
}
