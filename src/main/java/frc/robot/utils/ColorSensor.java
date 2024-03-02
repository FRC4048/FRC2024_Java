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
    private I2C.Port sensorPort;
    private ColorSensorV3 colorSensor;
    private ColorMatch colorMatcher50;
    private ColorMatch colorMatcherPointPoint1;
    private ColorMatch colorMatcherConcoction;
    private ColorMatch colorMatcherConfidence;

    public ColorSensor(I2C.Port sensorPort){
        this.sensorPort = sensorPort;
        colorSensor = new ColorSensorV3(sensorPort);

        colorMatcher50 = new ColorMatch();
        colorMatcherPointPoint1 = new ColorMatch();
        colorMatcherConcoction = new ColorMatch();
        colorMatcherConfidence = new ColorMatch();

        colorMatcher50.addColorMatch(ColorValue.Piece50.getColor());
        colorMatcherPointPoint1.addColorMatch(ColorValue.PiecePointPoint1.getColor());
        colorMatcherConcoction.addColorMatch(ColorValue.PlasticPieceConcoction.getColor());
        colorMatcher50.addColorMatch(ColorValue.Plastic.getColor());
        colorMatcherPointPoint1.addColorMatch(ColorValue.Plastic.getColor());
        colorMatcherConcoction.addColorMatch(ColorValue.Plastic.getColor());
        colorMatcherConfidence.addColorMatch(ColorValue.Piece50.getColor());
        colorMatcherConfidence.setConfidenceThreshold(Constants.COLORMATCH_CERTAINTY_NEEDED);
    }

    public ColorValue getColorConfidence() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match = colorMatcherConfidence.matchColor(detectedColor);
        if (match == null){
            return ColorValue.getFromColor(ColorValue.Plastic.getColor());
        }
        return ColorValue.getFromColor(match.color);
    }
    public ColorValue getColor50() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult match50 = colorMatcher50.matchClosestColor(detectedColor);

        if (match50 == null){
            return null;
        }
        return ColorValue.getFromColor(match50.color);
    }
    
    public ColorValue getColorPointPoint1() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult matchPointPoint1 = colorMatcherPointPoint1.matchClosestColor(detectedColor);
        if (matchPointPoint1 == null) {
            return null;
        }
        return ColorValue.getFromColor(matchPointPoint1.color);
    }
    public ColorValue getColorConcoction() {
        Color detectedColor = colorSensor.getColor();
        ColorMatchResult matchConcoction = colorMatcherConcoction.matchClosestColor(detectedColor);
        if (matchConcoction == null){
            return null;
        }
        return ColorValue.getFromColor(matchConcoction.color);
    }

    public Color getRawColor() {
        Color detectedColor = colorSensor.getColor();
        return detectedColor;
    }
    public double getMatchCertainty50() {
        ColorMatchResult match50 = colorMatcher50.matchClosestColor(colorSensor.getColor());
        return match50.confidence;
    }
    
    public double getMatchCertaintyPointPoint1() {
        ColorMatchResult matchPointPoint1 = colorMatcherPointPoint1.matchClosestColor(colorSensor.getColor());
        return matchPointPoint1.confidence;
    }
    public double getMatchCertaintyConcoction() {
        ColorMatchResult matchConcoction = colorMatcherConcoction.matchClosestColor(colorSensor.getColor());
        return matchConcoction.confidence; 
    }

}
