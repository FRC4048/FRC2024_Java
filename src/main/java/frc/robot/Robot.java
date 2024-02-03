// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.SetInitOdom;
import frc.robot.commands.drive.WheelAlign;

public class Robot extends TimedRobot {
    private RobotContainer robotContainer;
    private Command autoCommand;

    @Override
    public void robotInit() {
        robotContainer = new RobotContainer();
        new WheelAlign(robotContainer.getDrivetrain()).schedule();
        new ResetGyro(robotContainer.getDrivetrain(), 2).schedule();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void autonomousInit() {
        autoCommand = robotContainer.getAutoCommand();
        if (autoCommand!=null){
            autoCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {
        if (autoCommand != null) {
            autoCommand.cancel();
        }
    }
}
