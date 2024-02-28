// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter;

import frc.robot.subsystems.Shooter;
import frc.robot.utils.command.TimedSubsystemCommand;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class SetShooterSpeed extends TimedSubsystemCommand<Shooter> {
  /** Creates a new DummyShoot. */
  private double desiredLeftSpeedRpm;
  private double desiredRightSpeedRpm;
  public SetShooterSpeed(Shooter shooter) {
    super(shooter,5);
    SmartShuffleboard.put("Shooter", "Desired Left Speed", 0.0);
    SmartShuffleboard.put("Shooter", "Desired Right Speed", 0.0);

  }
  @Override
  public void initialize() {
    super.initialize();
    desiredLeftSpeedRpm = SmartShuffleboard.getDouble("Shooter", "Desired Left Speed", 0.0);
    desiredRightSpeedRpm = SmartShuffleboard.getDouble("Shooter", "Desired Right Speed", 0.0);
  }

  @Override
  public void execute() {
    getSystem().setShooterMotorLeftRPM(desiredLeftSpeedRpm);
    getSystem().setShooterMotorRightRPM(desiredRightSpeedRpm);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    getSystem().setShooterMotorLeftRPM(0.0);
    getSystem().setShooterMotorRightRPM(0.0);
  }

}
