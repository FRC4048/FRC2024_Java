// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.drivetrain.ResetGyro;
import frc.robot.commands.drivetrain.WheelAlign;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.constants.Constants;
import frc.robot.utils.TimeoutCounter;
import frc.robot.utils.diag.Diagnostics;
import frc.robot.utils.logging.CommandUtil;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Robot extends TimedRobot {
    private static Diagnostics diagnostics;
    private Command autonomousCommand;
    private double loopTime = 0;
    private double aliveTics = 0;

    private RobotContainer robotContainer;
    private Command autoCommand;

    @Override
    public void robotInit() {
        if (Constants.ENABLE_LOGGING) {
            DataLogManager.start();
            DriverStation.startDataLog(DataLogManager.getLog(), true);
            CommandScheduler.getInstance().onCommandInterrupt(command -> Logger.logInterruption(command.getName(), true));
        }
        diagnostics = new Diagnostics();
        robotContainer = new RobotContainer();
        CommandUtil.logged(new WheelAlign(robotContainer.getDrivetrain())).schedule();
        CommandUtil.logged(new ResetGyro(robotContainer.getDrivetrain(), 2)).schedule();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        double time = (loopTime == 0) ? 0 : (Timer.getFPGATimestamp() - loopTime) * 1000;
        Logger.logDouble("/robot/loopTime", time, Constants.ENABLE_LOGGING);

        // Shuffleboard commands
        SmartShuffleboard.put("Driver", "Has Game Piece?", robotContainer.getFeeder().pieceSeen(false)).withPosition(0, 0).withSize(2, 2);
        SmartShuffleboard.put("Driver", "Is Intake Deployer Raised?", robotContainer.getDeployer().isDeployerForwardLimitSwitchClosed()).withPosition(0, 2).withSize(2, 2);
        SmartShuffleboard.put("Driver", "Speaker Close", robotContainer.getRamp().isShootCloseAngle()).withPosition(7, 1).withSize(1, 1);
        SmartShuffleboard.put("Driver", "Speaker Away", robotContainer.getRamp().isShootAwayAngle()).withPosition(7, 2).withSize(1, 1);
        SmartShuffleboard.put("Driver", "Amp", robotContainer.getRamp().isShootAmpAngle()).withPosition(8, 2).withSize(1, 1);
    }

    @Override
    public void disabledInit() {
        aliveTics = 0;
        SmartDashboard.putNumber("TotalTimeouts", TimeoutCounter.getTotalTimeouts());
    }

    @Override
    public void disabledPeriodic() {
        SmartDashboard.putNumber("Alive",aliveTics);
        aliveTics = (aliveTics + 1) % 1000;
    }

    @Override
    public void autonomousInit() {
        CommandUtil.logged(new ResetRamp(robotContainer.getRamp())).schedule();
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
        CommandUtil.logged(new RaiseDeployer(robotContainer.getDeployer())).schedule();
        CommandUtil.logged(new ResetRamp(robotContainer.getRamp())).schedule();
    }

    @Override
    public void teleopPeriodic() {
        loopTime = Timer.getFPGATimestamp();
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
        diagnostics.reset();
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
