// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LightStrip;
import frc.robot.utils.logging.Logger;

/** Counts how many timeouts each command had during a match. */
public class TimeoutCounter {
    private int timeoutCounter = 0;
    private static int totalTimeouts = 0;
    private final String commandName;
    private final LightStrip lightStrip;

    public TimeoutCounter(String commandName, LightStrip lightStrip) {
        this.commandName = commandName;
        this.lightStrip = lightStrip;
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
    }
    public double getTimeoutCount() {
        return timeoutCounter;
    }
    public void increaseTimeoutCount() {
        timeoutCounter++;
        totalTimeouts++;
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
        lightStrip.setPattern(BlinkinPattern.HOT_PINK);
        double startTime = Timer.getFPGATimestamp();
        lightStrip.scheduleOnTrue(()-> Timer.getFPGATimestamp() - startTime >= 1, BlinkinPattern.BLACK);
    }
    public String getCommandName() {
        return commandName;
    }
    public void resetCounter() {
        timeoutCounter = 0;
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
    }
    public static int getTotalTimeouts(){
        return totalTimeouts;
    }
}
