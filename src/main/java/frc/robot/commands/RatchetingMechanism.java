// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ServoSubsystem;

public class RatchetingMechanism extends Command {
  private final ServoSubsystem servo;
  public RatchetingMechanism(ServoSubsystem servo) {
    this.servo = servo;
    addRequirements(servo);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (servo.get_Servo() == 0.0) {
      servo.set_Servo(1.0);
    } else if (servo.get_Servo() == 1.0) {
      servo.set_Servo(0.0);
    }
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
