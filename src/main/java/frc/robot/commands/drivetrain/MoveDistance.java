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
  private double speedX;
  private double speedY;
  private double maxSpeed;
  private double startPoseX;
  private double startPoseY;
  private SwerveDrivetrain drivetrain;
  private final double maxChangeX = 1.0; //TODO: Refine This Number
  private final double maxChangeY = 1.0; //TODO: Refine This Number
  private final double xChangeThreshhold = 0.01524; //TODO: Refine This Number
  private final double yChangeThreshhold = 0.01524; //TODO: Refine This Number

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
    startPoseX = drivetrain.getPose().getX();
    startPoseY = drivetrain.getPose().getY();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    speedY = 0;
    speedX = 0;
    if ((changeX == 0) && (changeY == 0)) {}
    else {
      speedX = (changeX * maxSpeed)/(Math.abs(changeX) + Math.abs(changeY));
      speedY = (changeY * maxSpeed)/(Math.abs(changeX) + Math.abs(changeY));
    }
    if ((Math.abs(changeX) <= maxChangeX) && (Math.abs(changeY) <= maxChangeY)) {
      drivetrain.drive(drivetrain.createChassisSpeeds(speedX, speedY, 0.0, Constants.FIELD_RELATIVE));
      System.out.println("I'm Driving");
      System.out.println(speedX);
      System.out.println(speedY);
    }
    else {
      System.out.println("Stopped");
    }
    System.out.println(speedX + " " + speedY);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    System.out.println((Timer.getFPGATimestamp() - startTime));
    System.out.println((startPoseX - drivetrain.getPose().getX()));
    System.out.println((startPoseY - drivetrain.getPose().getY()));
    if (Math.abs(startPoseX - drivetrain.getPose().getX()) > (Math.abs(changeX)) && Math.abs(startPoseY - drivetrain.getPose().getY()) > (Math.abs(changeY))) {
      return true;
    }
    return (((Timer.getFPGATimestamp() - startTime) >= 5) || ((Math.abs(changeX) >= maxChangeX) && (Math.abs(changeY) >= maxChangeY)));
  }
}
