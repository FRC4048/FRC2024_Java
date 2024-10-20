// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.TimeoutCounter;
import frc.robot.utils.loggingv2.LoggableCommand;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class SetShooterSpeed extends LoggableCommand {
  /** Creates a new DummyShoot. */
  private double startTime = Timer.getFPGATimestamp();
  private final Shooter shooter;
  private final LightStrip lightStrip;
  private double desiredLeftSpeedRpm;
  private double desiredRightSpeedRpm;
  private final TimeoutCounter timeoutCounter;
  private Timer timer = new Timer();

  public SetShooterSpeed(Shooter shooter, LightStrip lightStrip) {
    this.shooter = shooter;
    this.lightStrip = lightStrip;
    SmartShuffleboard.put("Shooter", "Desired Left Speed", 0.0);
    SmartShuffleboard.put("Shooter", "Desired Right Speed", 0.0);
    timeoutCounter = new TimeoutCounter(getName(), lightStrip);
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    desiredLeftSpeedRpm = SmartShuffleboard.getDouble("Shooter", "Desired Left Speed", 0.0);
    desiredRightSpeedRpm = SmartShuffleboard.getDouble("Shooter", "Desired Right Speed", 0.0);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    shooter.setShooterMotorLeftRPM(desiredLeftSpeedRpm);
    if (timer.getFPGATimestamp() - startTime > Constants.SHOOTER_MOTOR_STARTUP_OFFSET) {
      shooter.setShooterMotorRightRPM(desiredRightSpeedRpm);
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
