// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.constants.Constants;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.loggingv2.LoggableCommand;

public class CancelAll extends LoggableCommand {
  /** Creates a new CancelAll. */
  Ramp ramp;
  Shooter shooter;
  private final LightStrip lightStrip;

  public CancelAll(Ramp ramp, Shooter shooter, LightStrip lightStrip) {
    this.ramp = ramp;
    this.shooter = shooter;
    this.lightStrip = lightStrip;
    addRequirements(ramp, shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    ramp.setRampPos(Constants.RAMP_POS_STOW);
    shooter.setShooterMotorLeftRPM(0);
    shooter.setShooterMotorRightRPM(0);
    lightStrip.setPattern(BlinkinPattern.BLACK);
    CommandScheduler.getInstance().cancelAll();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
