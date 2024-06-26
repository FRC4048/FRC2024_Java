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
import frc.robot.commands.ramp.SetAutoRampPid;
import frc.robot.commands.teleOPinitReset;
import frc.robot.constants.Constants;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.RobotMode;
import frc.robot.utils.TimeoutCounter;
import frc.robot.utils.diag.Diagnostics;
import frc.robot.utils.logging.CommandUtil;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import java.util.concurrent.atomic.AtomicReference;

public class Robot extends TimedRobot {
    private static Diagnostics diagnostics;
    private Command autonomousCommand;
    private double loopTime = 0;
    private double aliveTics = 0;
    private Timer ledCycleTimer = new Timer();
    private Timer ledEndgameTimer = new Timer();

    private RobotContainer robotContainer;
    private Command autoCommand;
    private static final AtomicReference<RobotMode> mode = new AtomicReference<>(RobotMode.DISABLED);

    public static RobotMode getMode(){
        return mode.get();
    }

    @Override
    public void robotInit() {
        if (Constants.ENABLE_LOGGING) {
            DataLogManager.start();
            DriverStation.startDataLog(DataLogManager.getLog(), false);
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
//        if (ledEndgameTimer.hasElapsed(130)){
//            robotContainer.getLEDStrip().setPattern(BlinkinPattern.CONFETTI);
//        }
    }

    @Override
    public void disabledInit() {
        mode.set(RobotMode.DISABLED);
        aliveTics = 0;
        SmartShuffleboard.put("Driver","TotalTimeouts", TimeoutCounter.getTotalTimeouts()).withPosition(9, 3).withSize(1, 1);
    }

    @Override
    public void disabledPeriodic() {
        SmartDashboard.putNumber("Alive",aliveTics);
        aliveTics = (aliveTics + 1) % 1000;
    }

    @Override
    public void autonomousInit() {
        mode.set(RobotMode.AUTONOMOUS);
        ledEndgameTimer.restart();
        robotContainer.getLEDStrip().setPattern(RobotContainer.isRedAlliance() ? BlinkinPattern.HEARTBEAT_RED : BlinkinPattern.HEARTBEAT_BLUE);
        robotContainer.getRamp().setDefaultFF();
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
        mode.set(RobotMode.TELEOP);
        diagnostics.reset();
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        CommandUtil.logged(new RaiseDeployer(robotContainer.getDeployer(), robotContainer.getLEDStrip())).schedule();
        CommandUtil.parallel("Reset Climber and Ramp", new teleOPinitReset(robotContainer.getRamp(), robotContainer.getClimber(), robotContainer.getLEDStrip())).schedule();
        robotContainer.getRamp().setFarFF();
    }

    @Override
    public void teleopPeriodic() {
        loopTime = Timer.getFPGATimestamp();
    }

    @Override
    public void testInit() {
        mode.set(RobotMode.TEST);
        CommandScheduler.getInstance().cancelAll();
        diagnostics.reset();
        ledCycleTimer.restart();
    }

    @Override
    public void testPeriodic() {
        loopTime = 0;
        diagnostics.refresh();
        if (ledCycleTimer.advanceIfElapsed(0.5)){
            robotContainer.getLEDStrip().setPattern(robotContainer.getLEDStrip().getPattern().next());
        }
    }

    @Override
    public void simulationInit() {
        mode.set(RobotMode.SIMULATION);
    }

    @Override
    public void simulationPeriodic() {
        loopTime = Timer.getFPGATimestamp();
    }

    public static Diagnostics getDiagnostics() {
        return diagnostics;
    }
}
