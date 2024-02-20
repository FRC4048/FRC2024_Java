// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;

public class RotateBigRampWheel extends Command {
  /** Creates a new RotateBigRampWheel. */
  private final Ramp ramp;
  private double startPosition;
  private double startTime;
  public RotateBigRampWheel(Ramp ramp) {
    this.ramp = ramp;
    addRequirements(ramp);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startPosition = ramp.getPosition();
    startTime = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ramp.setMotor(0.3);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (ramp.getPosition() - startPosition >= (66) || Timer.getFPGATimestamp() - startTime >= 5);
  }
}
