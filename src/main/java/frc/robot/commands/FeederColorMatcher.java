// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import edu.wpi.first.wpilibj.Timer;

public class FeederColorMatcher extends Command {
  private Feeder feeder;
  private double startTime;
  /** Creates a new FeederColorMatcher. */
  public FeederColorMatcher(Feeder feeder) {
    this.feeder = feeder;
    addRequirements(feeder);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_SPEED);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    feeder.stopFeederMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (feeder.pieceSeen() || Timer.getFPGATimestamp() - startTime > 5.0);
  }
}
