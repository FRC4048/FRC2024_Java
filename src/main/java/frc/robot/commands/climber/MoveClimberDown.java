// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Climber;
import frc.robot.utils.TimeoutCounter;

public class MoveClimberDown extends Command {
  /** Creates a new MoveClimberDown. */
  private final Climber climber;
  private double starttime;
  private TimeoutCounter timeoutCounter = new TimeoutCounter(getName());
  public MoveClimberDown(Climber climber) {
    this.climber = climber;
    addRequirements(climber);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    starttime = Timer.getFPGATimestamp();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    climber.setSpeed(-Constants.CLIMBER_SPEED);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Timer.getFPGATimestamp() - starttime > 3) {
      timeoutCounter.increaseTimeoutCount();
      return true;
    }
    return (climber.isRightFullyRetracted() && climber.isLeftFullyRetracted());
  }
}
