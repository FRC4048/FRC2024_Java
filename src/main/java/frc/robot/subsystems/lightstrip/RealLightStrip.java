package frc.robot.subsystems.lightstrip;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import frc.robot.constants.Constants;
import frc.robot.utils.BlinkinPattern;

public class RealLightStrip implements LightstripIO {

    private final Spark colorSensorPort;

    public RealLightStrip() {
        colorSensorPort = new Spark(Constants.LIGHTSTRIP_PORT);
    }

    @Override
    public void setPattern(BlinkinPattern pattern) {
        colorSensorPort.set(pattern.getPwm());
    }

    @Override
    public void updateInputs(LightStripInputs inputs) {
        inputs.pattern = BlinkinPattern.of(colorSensorPort.get());
    }
}
