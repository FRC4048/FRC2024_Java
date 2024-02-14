// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;

public class ResetRamp extends Command {
  /** Creates a new ResetRamp. */
  private Ramp ramp; 
  private double startTime;

  public ResetRamp(Ramp ramp) {
    this.ramp = ramp;
    addRequirements(ramp);
    startTime = Timer.getFPGATimestamp();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    ramp.changeRampPos(99);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    ramp.resetEncoder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return ((ramp.getForwardSwitchState()) || ((Timer.getFPGATimestamp() - startTime) >= 0.5));
  }
}
