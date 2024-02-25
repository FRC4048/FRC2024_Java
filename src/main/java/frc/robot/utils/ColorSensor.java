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
    private ColorMatch colorMatcher50;
    private ColorMatch colorMatcher25;
    private ColorMatch colorMatcher10;
    private ColorMatch colorMatcher5;

    public ColorSensor(I2C.Port sensorPort){
        this.sensorPort = sensorPort;
        colorSensor = new ColorSensorV3(sensorPort);

        colorMatcher50 = new ColorMatch();
        colorMatcher25 = new ColorMatch();
        colorMatcher10 = new ColorMatch();
        colorMatcher5 = new ColorMatch();

        colorMatcher50.addColorMatch(ColorValue.Piece50.getColor());
        colorMatcher25.addColorMatch(ColorValue.Piece25.getColor());
        colorMatcher10.addColorMatch(ColorValue.Piece10.getColor());
        colorMatcher5.addColorMatch(ColorValue.Piece5.getColor());
        colorMatcher50.addColorMatch(ColorValue.Plastic.getColor());
        colorMatcher25.addColorMatch(ColorValue.Plastic.getColor());
        colorMatcher10.addColorMatch(ColorValue.Plastic.getColor());
        colorMatcher5.addColorMatch(ColorValue.Plastic.getColor());
    }

    /**
     * @return the matched color from the sensor or null if none found
     */
    public ColorValue getColor50() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match50 = colorMatcher50.matchClosestColor(detectedColor);

        if (match50 == null){
            return null;
        }
        return ColorValue.getFromColor(match50.color);
    }
    public ColorValue getColor25() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match25 = colorMatcher25.matchClosestColor(detectedColor);
        if (match25 == null){
            return null;
        }
        return ColorValue.getFromColor(match25.color);
    }
    public ColorValue getColor10() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match10 = colorMatcher10.matchClosestColor(detectedColor);
        if (match10 == null){
            return null;
        }
        return ColorValue.getFromColor(match10.color);
    }
    public ColorValue getColor5() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match5 = colorMatcher5.matchClosestColor(detectedColor);
        if (match5 == null){
            return null;
        }
        return ColorValue.getFromColor(match5.color);
    }

    public Color getRawColor() {
        Color detectedColor = colorSensor.getColor();
        return detectedColor;
    }
    public double getMatchCertainity50() {
        ColorMatchResult match50 = colorMatcher50.matchClosestColor(colorSensor.getColor());
        return match50.confidence;
    }
    public double getMatchCertainity25() {
        ColorMatchResult match25 = colorMatcher25.matchClosestColor(colorSensor.getColor());
        return match25.confidence;
    }
    public double getMatchCertainity10() {
        ColorMatchResult match10 = colorMatcher10.matchClosestColor(colorSensor.getColor());
        return match10.confidence;
    }
    public double getMatchCertainity5() {
        ColorMatchResult match5 = colorMatcher5.matchClosestColor(colorSensor.getColor());
        return match5.confidence;
    }

}
