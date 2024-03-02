/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utils.diag;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.utils.ColorSensor;
import frc.robot.utils.ColorValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Add your docs here.
 */
public class DiagColorSensor implements Diagnosable {

    private ColorSensor colorsensor;
    private String title;
    private String name;
    private GenericEntry networkTableEntry;
    private Map<ColorValue, Boolean> colorMap;

    public DiagColorSensor(String title, String name, ColorSensor colorsensor) {
        this.title = title;
        this.name = name;
        this.colorsensor = colorsensor;
        colorMap = new HashMap<ColorValue, Boolean>();
        reset();
    }

    @Override
    public void setShuffleBoardTab(ShuffleboardTab shuffleBoardTab, int width, int height) {
        networkTableEntry = shuffleBoardTab.getLayout(title, BuiltInLayouts.kList).withSize(width, height).add(name, false).getEntry();
    }

    @Override
    public void refresh() {
        ColorValue colorValue = colorsensor.getColor();
        colorMap.put(colorValue, true);
        boolean allColors = colorMap.values().stream().allMatch(value -> (value == true));
        if (networkTableEntry != null) {
            networkTableEntry.setBoolean(allColors);
        }
    }

    @Override
    public void reset() {
        Arrays.stream(ColorValue.values()).forEach(color -> colorMap.put(color, false));
    }
}
