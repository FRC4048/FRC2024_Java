// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import edu.wpi.first.wpilibj.Timer;
//import frc.robot.subsystems.lightstrip.LightStrip;
import org.littletonrobotics.junction.Logger;

/** Counts how many timeouts each command had during a match. */
public class TimeoutCounter {
    private int timeoutCounter = 0;
    private static int totalTimeouts = 0;
    private final String commandName;
    //private final LightStrip lightStrip;

    public TimeoutCounter(String commandName) {
        this.commandName = commandName;
        //this.lightStrip = lightStrip;
        Logger.recordOutput("Timeouts/" + commandName, timeoutCounter);
    }
    public double getTimeoutCount() {
        return timeoutCounter;
    }
    public void increaseTimeoutCount() {
        timeoutCounter++;
        totalTimeouts++;
        Logger.recordOutput("Timeouts/" + commandName, timeoutCounter);
        //lightStrip.setPattern(BlinkinPattern.HOT_PINK);
        double startTime = Timer.getFPGATimestamp();
        //lightStrip.scheduleOnTrue(()-> Timer.getFPGATimestamp() - startTime >= 1, BlinkinPattern.BLACK);
    }
    public String getCommandName() {
        return commandName;
    }
    public void resetCounter() {
        timeoutCounter = 0;
        Logger.recordOutput("Timeouts/" + commandName, timeoutCounter);
    }
    public static int getTotalTimeouts(){
        return totalTimeouts;
    }
}