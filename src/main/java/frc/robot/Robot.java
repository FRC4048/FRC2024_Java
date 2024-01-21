// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.ResetGyro;
import frc.robot.commands.drive.WheelAlign;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.diag.Diagnostics;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command autonomousCommand;
    private double loopTime = 0;
    private RobotContainer robotContainer;
    private static Diagnostics diagnostics;


    @Override
    public void robotInit() {
        if (Constants.ENABLE_LOGGING) {
            DataLogManager.start();
            DriverStation.startDataLog(DataLogManager.getLog(), true);
        }
        diagnostics = new Diagnostics();
        robotContainer = new RobotContainer();

        new ResetGyro(robotContainer.getDrivetrain(), 2).schedule();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        // Logger should stay at the end of robotPeriodic()
        double time = (loopTime == 0) ? 0 : (Timer.getFPGATimestamp() - loopTime) * 1000;
        Logger.logDouble("/robot/loopTime", time, Constants.ENABLE_LOGGING);
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
