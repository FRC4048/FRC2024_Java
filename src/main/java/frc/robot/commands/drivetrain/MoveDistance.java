// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drivetrain;

import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.command.TimedSubsystemCommand;

public class MoveDistance extends TimedSubsystemCommand<SwerveDrivetrain> {
  /** Creates a new MoveDistance. */
  private final double changeXMeters;
  private final double changeYMeters;
  private final double maxSpeed;
  private double desiredPoseX;
  private double desiredPoseY;
  public MoveDistance(SwerveDrivetrain drivetrain, double changeXMeters, double changeYMeters, double maxSpeed) {
    super(drivetrain,Constants.MOVE_DISTANCE_TIMEOUT);
    this.changeXMeters = changeXMeters;
    this.changeYMeters = changeYMeters;
    this.maxSpeed = Math.abs(maxSpeed);
  }

  @Override
  public void initialize() {
    super.initialize();
    desiredPoseX = getSystem().getPose().getX() + changeXMeters;
    desiredPoseY = getSystem().getPose().getY() + changeYMeters;
  }

  @Override
  public void execute() {
    double speedY = 0;
    double speedX = 0;
    double neededChangeX = desiredPoseX - getSystem().getPose().getX();
    double neededChangeY = desiredPoseY - getSystem().getPose().getY();
    if ((neededChangeX != 0) || (neededChangeY != 0)) {
      speedX = (neededChangeX * maxSpeed) / (Math.abs(neededChangeX) + Math.abs(neededChangeY));
      speedY = (neededChangeY * maxSpeed) / (Math.abs(neededChangeX) + Math.abs(neededChangeY));
    }
    getSystem().drive(getSystem().createChassisSpeeds(speedX, speedY, 0.0, Constants.FIELD_RELATIVE));
  }

  @Override
  public void end(boolean interrupted) {
    getSystem().stopMotor();
  }

  @Override
  public boolean isFinished() {
    double targetXDistance = Math.abs((getSystem().getPose().getX() - desiredPoseX));
    double targetYDistance = Math.abs((getSystem().getPose().getY() - desiredPoseY));
    return  super.isFinished() || ((targetXDistance <= Constants.DRIVE_THRESHHOLD_METERS && targetYDistance <= Constants.DRIVE_THRESHHOLD_METERS));
  }
}
