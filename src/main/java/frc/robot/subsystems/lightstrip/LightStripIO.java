package frc.robot.subsystems.lightstrip;

import frc.robot.subsystems.LoggableIO;
import frc.robot.utils.BlinkinPattern;

public interface LightStripIO extends LoggableIO<LightStripInputs> {
    void setPattern(BlinkinPattern pattern);
}
