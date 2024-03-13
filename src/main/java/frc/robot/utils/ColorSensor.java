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
import frc.robot.constants.Constants;

import java.util.Arrays;

/**
 * Add your docs here.
 */
public class ColorSensor {
    private ColorSensorV3 colorSensor;
    private ColorMatch colorMatcher;
    private Color previousColor;

    public ColorSensor(I2C.Port sensorPort){
        colorSensor = new ColorSensorV3(sensorPort);
        colorMatcher = new ColorMatch();
        Arrays.stream(ColorValue.values()).forEach(c -> colorMatcher.addColorMatch(c.getColor()));
    }

    /**
     * @return the matched color from the sensor or null if none found
     */
    public ColorValue getColor() {
        Color currentColor = getRawColor();
        double redChange = previousColor.red - currentColor.red;
        double greenChange = previousColor.green - currentColor.green;
        double blueChange = previousColor.blue - currentColor.blue;
        double change = Math.sqrt(redChange*redChange + greenChange*greenChange + blueChange*blueChange);
        previousColor = currentColor;
        if (change > Constants.COLOR_SENSOR_CHANGE_THRESHOLD) {
            return ColorValue.Piece;
        } else {
            return null;
        }
    }

    public ColorMatchResult getMatchedColor() {
        Color detectedColor = colorSensor.getColor();
        return colorMatcher.matchClosestColor(detectedColor);
    }

    public Color getRawColor() {
        return colorSensor.getColor();
    } 
}
