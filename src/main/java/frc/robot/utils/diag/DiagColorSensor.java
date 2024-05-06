/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utils.diag;

import frc.robot.constants.Constants;
import frc.robot.subsystems.feeder.ColorSensor;

/**
 * Add your docs here.
 */
public class DiagColorSensor extends DiagBoolean {

    private final ColorSensor colorsensor;

    public DiagColorSensor(String title, String name, ColorSensor colorsensor) {
        super(title,name);
        this.colorsensor = colorsensor;
        reset();
    }

    @Override
    protected boolean getValue() {
        return colorsensor.getMatchedColor().confidence > Constants.COLOR_CONFIDENCE_RATE_INCOMING;
    }
}
