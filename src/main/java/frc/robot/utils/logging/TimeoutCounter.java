// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils.logging;

import frc.robot.constants.Constants;

/** Counts how many timeouts each command had during a match. */
public class TimeoutCounter {
    private int timeoutCounter = 0;
    private final String commandName;
    public TimeoutCounter(String commandName) {
        this.commandName = commandName;
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
    }
    public double getTimeoutCount() {
        return timeoutCounter;
    }
    public void increaseTimeoutCount() {
        timeoutCounter++;
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
    }
    public String getCommandName() {
        return commandName;
    }
    public void resetCounter() {
        timeoutCounter = 0;
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
    }
}
