// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.ramp;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.command.TimedSubsystemCommand;

public class ResetRamp extends TimedSubsystemCommand<Ramp> {

  public ResetRamp(Ramp ramp) {
    super(ramp,Constants.RESET_RAMP_TIMEOUT);
  }

  @Override
  public void initialize() {
    super.initialize();
    getSystem().setMotor(Constants.RESET_RAMP_SPEED);
  }

  @Override
  public void end(boolean interrupted) {
    getSystem().stopMotor();
    getSystem().resetEncoder();
  }
  @Override
  public boolean isFinished() {
    return (getSystem().getReversedSwitchState()) || super.isFinished();
  }

  @Override
  public boolean runsWhenDisabled() {
    return true;
  }
}
