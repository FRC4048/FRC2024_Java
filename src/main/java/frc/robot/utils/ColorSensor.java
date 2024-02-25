/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utils;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import java.util.Arrays;

/**
 * Add your docs here.
 */
public class ColorSensor {
    private I2C.Port sensorPort;
    private ColorSensorV3 colorSensor;
    private ColorMatch colorMatcher;

    public ColorSensor(I2C.Port sensorPort){
        this.sensorPort = sensorPort;
        colorSensor = new ColorSensorV3(sensorPort);

        colorMatcher = new ColorMatch();
        Arrays.stream(ColorValue.values()).forEach(c -> colorMatcher.addColorMatch(c.getColor()));
    }

    /**
     * @return the matched color from the sensor or null if none found
     */
    public ColorValue getColor() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);

        if (match == null){
            return null;
        }
        return ColorValue.getFromColor(match.color);
    }

    public ColorMatchResult getRawColor() {
        Color detectedColor = colorSensor.getColor();
        return colorMatcher.matchClosestColor(detectedColor);
    }
}
