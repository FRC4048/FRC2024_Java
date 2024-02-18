// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;

public class MoveDistance extends Command {
  /** Creates a new MoveDistance. */
  private double startTime;
  private double changeX;
  private double changeY;
  private double maxSpeed;
  private double desiredPoseX;
  private double desiredPoseY;
  private SwerveDrivetrain drivetrain;
  private final double xChangeThreshhold = 0.0762; // TODO: Refine This Number
  private final double yChangeThreshhold = 0.0762; // TODO: Refine This Number

  public MoveDistance(SwerveDrivetrain drivetrain, double changeX, double changeY, double maxSpeed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drivetrain = drivetrain;
    this.changeX = changeX;
    this.changeY = changeY;
    this.maxSpeed = Math.abs(maxSpeed);
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    desiredPoseX = drivetrain.getPose().getX() + changeX;
    desiredPoseY = drivetrain.getPose().getY() + changeY;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speedY = 0;
    double speedX = 0;
    changeX = desiredPoseX - drivetrain.getPose().getX();
    changeY = desiredPoseY - drivetrain.getPose().getY();
    if ((changeX != 0) || (changeY != 0)) {
      speedX = (changeX * maxSpeed) / (Math.abs(changeX) + Math.abs(changeY));
      speedY = (changeY * maxSpeed) / (Math.abs(changeX) + Math.abs(changeY));
    }
    drivetrain.drive(drivetrain.createChassisSpeeds(speedX, speedY, 0.0, Constants.FIELD_RELATIVE));
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(drivetrain.getPose().getX() - desiredPoseX) <= xChangeThreshhold && Math.abs(drivetrain.getPose().getY() - desiredPoseY) <= yChangeThreshhold) {
      return true;
    }
    return ((Timer.getFPGATimestamp() - startTime) >= 5);
  }
}
