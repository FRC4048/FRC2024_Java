// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake2025;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Timer;

public class Intake2025Command extends Command {
  /** Creates a new Intake2025Command. */
  private final Intake2025 intake;
  private double time;
  private DoubleSupplier speed1;
  private DoubleSupplier speed2;
  public Intake2025Command(Intake2025 intake, DoubleSupplier speed1, DoubleSupplier speed2) {
    this.intake = intake;
    this.speed1 = speed1;
    this.speed2 = speed2;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    time = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.setIntakeMotor1Speed(speed1.getAsDouble());
    intake.setIntakeMotor2Speed(speed2.getAsDouble());
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntakeMotors();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Timer.getFPGATimestamp()-time > 5) {
      return true;
    } else {
      return false;
    }
  } 
}
