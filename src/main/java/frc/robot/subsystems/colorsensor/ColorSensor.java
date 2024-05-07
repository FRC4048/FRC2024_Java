/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.colorsensor;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj.util.Color;
import frc.robot.Robot;
import frc.robot.subsystems.LoggableSystem;
import frc.robot.utils.ColorValue;
import frc.robot.utils.diag.DiagColorSensor;

import java.util.Arrays;

/**
 * Add your docs here.
 */
public class ColorSensor {
    private final LoggableSystem<ColorSensorIO, ColorSensorInputs> system;
    private final ColorMatch colorMatcher;


    public ColorSensor(ColorSensorIO colorSensorIO){
        this.system = new LoggableSystem<>(colorSensorIO, new ColorSensorInputs());
        colorMatcher = new ColorMatch();
        Arrays.stream(ColorValue.values()).forEach(c -> colorMatcher.addColorMatch(c.getColor()));
        Robot.getDiagnostics().addDiagnosable(new DiagColorSensor("Feeder", "Color Sensor", this));
    }

    public void updateInputs(){
        system.updateInputs();
    }
    /**
     * @return the matched color from the sensor or null if none found
     */
    public ColorValue getColor() {
        Color detectedColor = system.getInputs().rawColor;
        ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);

        if (match == null){
            return null;
        }
        return ColorValue.getFromColor(match.color);
    }

    public ColorMatchResult getMatchedColor() {
        Color detectedColor = system.getInputs().rawColor;
        return colorMatcher.matchClosestColor(detectedColor);
    }

    public Color getRawColor() {
        return system.getInputs().rawColor;
    }
}
