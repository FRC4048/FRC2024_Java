// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Ramp;

public class ResetRamp extends Command {
  /** Creates a new ResetRamp. */
  private Ramp ramp; 
  private double startTime;
  private Double rampStopTime = null;

  /*
   *When we get the robot:
   *TODO: Check if the forward limit switch is the top limit switch otherwise, swap getForwardSwitchState() with getReversedSwitchState()
   *TODO: Check if the motor pulls the cannon up otherwise multiply the value by negative one
   *TODO: Check if the motor is at a reasonable speed
   */

  public ResetRamp(Ramp ramp) {
    this.ramp = ramp;
    addRequirements(ramp);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    startTime = Timer.getFPGATimestamp();
    ramp.setMotor(Constants.RESET_RAMP_SPEED); //assuming positive is forward with a random speed
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    ramp.stopMotor();
    if(ramp.getReversedSwitchState()) ramp.resetEncoder();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (ramp.getReversedSwitchState()) {
      ramp.stopMotor();
    if(rampStopTime.equals(null)) rampStopTime = Timer.getFPGATimestamp();
    else if ((Timer.getFPGATimestamp() - rampStopTime) >= Constants.RAMP_WIND_DOWN_TIME) return true;
    }
    else if ((Timer.getFPGATimestamp() - startTime) >= 5) {
      return true;
    }
    return false;
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
