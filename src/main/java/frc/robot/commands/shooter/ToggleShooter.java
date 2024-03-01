// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;

public class ToggleShooter extends Command {
  /** Creates a new ToggleShooter. */
  private final Shooter shooter;
  
  public ToggleShooter(Shooter shooter) {
    this.shooter = shooter;
    addRequirements(shooter);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    if (Math.abs(shooter.getShooterMotorLeftRPM()) >= Constants.SHOOTER_MOTOR_SPEED_TRESHOLD) {
      shooter.setShooterMotorLeftRPM(shooter.getLastMotorLeftSpeed());
      shooter.setShooterMotorRightRPM(shooter.getLastMotorRightSpeed());
    }
    else {
      shooter.stopShooter();
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
