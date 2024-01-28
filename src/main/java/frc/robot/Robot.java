// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.SetInitOdom;
import frc.robot.commands.drive.WheelAlign;
import frc.robot.utils.diag.Diagnostics;
import frc.robot.utils.logging.Logger;

public class Robot extends TimedRobot {
    private static Diagnostics diagnostics;
    private Command autonomousCommand;
    private double loopTime = 0;

    private RobotContainer robotContainer;
    private Command autoCommand;

    @Override
    public void robotInit() {
        if (Constants.ENABLE_LOGGING) {
            DataLogManager.start();
            DriverStation.startDataLog(DataLogManager.getLog(), true);
        }
        diagnostics = new Diagnostics();
        robotContainer = new RobotContainer();
        new WheelAlign(robotContainer.getDrivetrain()).schedule();
        new ResetGyro(robotContainer.getDrivetrain(), 2).schedule();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        double time = (loopTime == 0) ? 0 : (Timer.getFPGATimestamp() - loopTime) * 1000;
        Logger.logDouble("/robot/loopTime", time, Constants.ENABLE_LOGGING);
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    @Override
    public void autonomousInit() {
        autonomousCommand = robotContainer.getAutoCommand();
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
        loopTime = Timer.getFPGATimestamp();
    }

    @Override
    public void teleopInit() {
        diagnostics.reset();
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {
        loopTime = Timer.getFPGATimestamp();
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {
        loopTime = 0;
        diagnostics.refresh();
    }

    @Override
    public void simulationPeriodic() {
        loopTime = Timer.getFPGATimestamp();
    }

    public static Diagnostics getDiagnostics() {
        return diagnostics;
    }
}
