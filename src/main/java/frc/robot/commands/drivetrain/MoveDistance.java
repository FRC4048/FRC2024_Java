// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;

public class MoveDistance extends Command {
  /** Creates a new MoveDistance. */
  private double startTime;
  private double changeXMeters;
  private double changeYMeters;
  private double maxSpeed;
  private double desiredPoseX;
  private double desiredPoseY;
  private SwerveDrivetrain drivetrain;

  public MoveDistance(SwerveDrivetrain drivetrain, double changeXMeters, double changeYMeters, double maxSpeed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drivetrain = drivetrain;
    this.changeXMeters = changeXMeters;
    this.changeYMeters = changeYMeters;
    this.maxSpeed = Math.abs(maxSpeed);
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    desiredPoseX = drivetrain.getPose().getX() + changeXMeters;
    desiredPoseY = drivetrain.getPose().getY() + changeYMeters;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double speedY = 0;
    double speedX = 0;
    double neededChangeX = desiredPoseX - drivetrain.getPose().getX();
    double neededChangeY = desiredPoseY - drivetrain.getPose().getY();
    if ((neededChangeX != 0) || (neededChangeY != 0)) {
      speedX = (neededChangeX * maxSpeed) / (Math.abs(neededChangeX) + Math.abs(neededChangeY));
      speedY = (neededChangeY * maxSpeed) / (Math.abs(neededChangeX) + Math.abs(neededChangeY));
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

    double targetXDistance = Math.abs((drivetrain.getPose().getX() - desiredPoseX));
    double targetYDistance = Math.abs((drivetrain.getPose().getY() - desiredPoseY));
    if (targetXDistance <= Constants.DRIVE_THRESHHOLD_METERS && targetYDistance <= Constants.DRIVE_THRESHHOLD_METERS) {
      return true;
    }
    return ((Timer.getFPGATimestamp() - startTime) >= 5);
  }
}
