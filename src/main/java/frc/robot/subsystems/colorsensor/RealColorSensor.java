package frc.robot.subsystems.colorsensor;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

public class RealColorSensor implements ColorSensorIO{

    private final ColorSensorV3 colorSensor;

    public RealColorSensor() {
        this.colorSensor = new ColorSensorV3(I2C.Port.kMXP);
    }

    @Override
    public void updateInputs(ColorSensorInputs inputs) {
        ColorSensorV3.RawColor rawColor = colorSensor.getRawColor();
        inputs.rawColor = new Color(rawColor.red, rawColor.green, rawColor.blue);
    }
}
