// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.ColorSensor;
import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj.Timer;

public class FeederColorMatcher extends Command {
  private Feeder feeder;
  private ColorSensor colorSensor;
  private double startTime;
  /** Creates a new FeederColorMatcher. */
  public FeederColorMatcher(Feeder feeder, ColorSensor colorSensor) {
    this.feeder = feeder;
    this.colorSensor = colorSensor;
    addRequirements(feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_SPEED);
    startTime = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    feeder.stopFeederMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (colorSensor.pieceSeen() || Timer.getFPGATimestamp() - startTime > 5.0);
  }
}
