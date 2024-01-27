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
    private final RobotContainer robotContainer;
    private Command autoCommand;
    public Robot (){
        robotContainer = new RobotContainer();
    }

    @Override
    public void robotInit() {
        new WheelAlign(robotContainer.getDrivetrain()).schedule();
        new SequentialCommandGroup(
                new ResetGyro(robotContainer.getDrivetrain(), 2),
                new SetInitOdom(robotContainer.getDrivetrain(),robotContainer.getAutoChooser())
        ).schedule();


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
}
