// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
import frc.robot.utils.loggingv2.CommandLogger;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class Robot extends LoggedRobot {

public class Robot extends TimedRobot {
    private static Diagnostics diagnostics;
    private Command autonomousCommand;
    private double aliveTics = 0;
    private final Timer ledCycleTimer = new Timer();
    private final Timer ledEndgameTimer = new Timer();
    private RobotContainer robotContainer;
    private static final ConcurrentLinkedQueue<Runnable> runInMainThread = new ConcurrentLinkedQueue<>();

    private static final AtomicReference<RobotMode> mode = new AtomicReference<>(RobotMode.DISABLED);

    public static RobotMode getMode(){
        return mode.get();
    }

    @Override
    public void robotInit() {
        if (Constants.ENABLE_LOGGING) {
            Logger.recordMetadata("ProjectName", "FRC2024_Java"); // Set a metadata value
            Logger.recordMetadata("GitSHA", BuildConstants.GIT_SHA);
            if (isReal()) {
                Logger.addDataReceiver(new WPILOGWriter()); // Log to a USB stick ("/U/logs")
            } else {
                setUseTiming(false); // Run as fast as possible
                String logPath = LogFileUtil.findReplayLog(); // Pull the replay log from AdvantageScope (or prompt the user)
                Logger.setReplaySource(new WPILOGReader(logPath)); // Read replay log
                Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim"))); // Save outputs to a new log
            }
            Logger.start(); // Start logging! No more data receivers, replay sources, or metadata values may be added.
            // Log active commands
            CommandLogger.get().init();
        }
        diagnostics = new Diagnostics();
        robotContainer = new RobotContainer();
        new WheelAlign(robotContainer.getDrivetrain()).schedule();
        new ResetGyro(robotContainer.getDrivetrain(), 2).schedule();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        if (Constants.ENABLE_LOGGING){
            CommandLogger.get().log();
            long startTime = Logger.getRealTimestamp();
            Runnable poll = runInMainThread.poll();
            while (poll != null){
                poll.run();
                if (Logger.getRealTimestamp() - startTime <= 3000){
                    poll = runInMainThread.poll();
                }else {
                    break;
                }
            }
        }

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
    }

    @Override
    public void teleopInit() {
        mode.set(RobotMode.TELEOP);
        diagnostics.reset();
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        new RaiseDeployer(robotContainer.getDeployer(), robotContainer.getLEDStrip()).schedule();
        new teleOPinitReset(robotContainer.getRamp(), robotContainer.getClimber(), robotContainer.getLEDStrip()).withBasicName("Reset Climber and Ramp").schedule();
        robotContainer.getRamp().setFarFF();
    }

    @Override
    public void teleopPeriodic() {
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
    }

    public static Diagnostics getDiagnostics() {
        return diagnostics;
    }
    public static void runInMainThread(Runnable r){
        runInMainThread.add(r);
    }
}
