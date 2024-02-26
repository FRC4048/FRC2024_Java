// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveDrivetrain;

public class CancelAll extends Command {
  /** Creates a new CancelAll. */
  SwerveDrivetrain drivetrian;
  public CancelAll(SwerveDrivetrain drivetrian) {
    this.drivetrian = drivetrian;
    addRequirements(drivetrian);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drivetrian.stopMotor();
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
