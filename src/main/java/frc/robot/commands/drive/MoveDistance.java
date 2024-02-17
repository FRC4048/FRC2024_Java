// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;

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
  private final double maxChangeX  = 1.0;
  private final double maxChangeY = 1.0;
  private double xChangeThreshhold = 0.1524;
  private double yChangeThreshhold = 0.1524;

  public MoveDistance(SwerveDrivetrain drivetrain, double changeX, double changeY, double maxSpeed) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.drivetrain = drivetrain;
    this.changeX = changeX;
    this.changeY = changeY;
    this.maxSpeed = maxSpeed;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    startPoseX = drivetrain.getPose().getX();
    startPoseY = drivetrain.getPose().getY();

    if (Math.abs(changeX) > Math.abs(changeY)) {
      speedX = Math.signum(changeX) * maxSpeed;
      double ratio = changeX / maxSpeed;
      speedY = changeY / ratio;
    } 
    else {
      speedY = Math.signum(changeY) * maxSpeed;
      double ratio = changeY / maxSpeed;
      speedX = changeX / ratio;
    }
    if ((changeX <= maxChangeX) && (changeX <= maxChangeY)) drivetrain.drive(drivetrain.createChassisSpeeds(speedX, speedY, 0.0, true));
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.stopMotor();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(startPoseX - drivetrain.getPose().getX()) > (Math.abs(changeX) + xChangeThreshhold) && Math.abs(startPoseY - drivetrain.getPose().getY()) > (Math.abs(changeY) + yChangeThreshhold)) {
      return true;
    }
    return (((Timer.getFPGATimestamp() - startTime) >= 2) || !((changeX <= maxChangeX) && (changeX <= maxChangeY)));
  }
}
