// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

import frc.robot.constants.Constants;
import frc.robot.utils.logging.Logger;

/** Counts how many timeouts each command had during a match. */
public class TimeoutCounter {
    private int timeoutCounter = 0;
    private final String commandName;
    public TimeoutCounter(String commandName) {
        this.commandName = commandName;
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
        System.out.println("Constructed");
    }
    public double getTimeoutCount() {
        System.out.println("Got TimeoutCounter");
        return timeoutCounter;
    }
    public void increaseTimeoutCount() {
        timeoutCounter++;
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
        System.out.println("Logged Increase Timeout");
    }
    public String getCommandName() {
        return commandName;
    }
    public void resetCounter() {
        timeoutCounter = 0;
        System.out.println("Logged reset Timeout");
        Logger.logInteger("/Timeouts/" + commandName, timeoutCounter, Constants.ENABLE_LOGGING);
    }
}
