// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.TimeoutCounter;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class SetShooterSpeed extends Command {
  /** Creates a new DummyShoot. */
  private double startTime = Timer.getFPGATimestamp();
  private final Shooter shooter;
  private final LightStrip lightStrip;
  private double desiredLeftSpeedRpm;
  private double desiredRightSpeedRpm;
  private final TimeoutCounter timeoutCounter;
  private Timer timer;
  private boolean leftStarted;
  private boolean rightStarted;
  public SetShooterSpeed(Shooter shooter, LightStrip lightStrip) {
    this.shooter = shooter;
    this.lightStrip = lightStrip;
    SmartShuffleboard.put("Shooter", "Desired Left Speed", 0.0);
    SmartShuffleboard.put("Shooter", "Desired Right Speed", 0.0);
    timeoutCounter = new TimeoutCounter(getName(), lightStrip);
    addRequirements(shooter);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    desiredLeftSpeedRpm = SmartShuffleboard.getDouble("Shooter", "Desired Left Speed", 0.0);
    desiredRightSpeedRpm = SmartShuffleboard.getDouble("Shooter", "Desired Right Speed", 0.0);
    leftStarted = false;
    rightStarted = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (!leftStarted) {
      shooter.setShooterMotorLeftRPM(desiredLeftSpeedRpm);
      leftStarted = true;
    } 
    if (timer.getFPGATimestamp() - startTime > 0.2 && !rightStarted) {
      shooter.setShooterMotorRightRPM(desiredRightSpeedRpm);
      rightStarted = true;
    }
    if (shooter.upToSpeed(desiredLeftSpeedRpm, desiredRightSpeedRpm)){
      lightStrip.setPattern(BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.slowStop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if ((Timer.getFPGATimestamp() - startTime) >= 5) {
      timeoutCounter.increaseTimeoutCount();
      return true;
    }
    return false;
  }
}
