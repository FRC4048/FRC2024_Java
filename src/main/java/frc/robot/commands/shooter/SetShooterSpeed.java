// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.TimeoutCounter;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class SetShooterSpeed extends Command {
  /** Creates a new DummyShoot. */
  private double startTime = Timer.getFPGATimestamp();
  private final Shooter shooter;
  private double desiredLeftSpeedRpm;
  private double desiredRightSpeedRpm;
  private final TimeoutCounter timeoutCounter = new TimeoutCounter(getName());
  private Timer timer;
  private boolean leftStarted;
  private boolean rightStarted;
  public SetShooterSpeed(Shooter shooter) {
    this.shooter = shooter;
    addRequirements(shooter);
    SmartShuffleboard.put("Shooter", "Desired Left Speed", 0.0);
    SmartShuffleboard.put("Shooter", "Desired Right Speed", 0.0);
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
    if (timer.getFPGATimestamp() - startTime > 0.1 && !rightStarted) {
      shooter.setShooterMotorRightRPM(desiredRightSpeedRpm);
      rightStarted = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    shooter.setShooterMotorLeftRPM(0.0);
    shooter.setShooterMotorRightRPM(0.0);
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
