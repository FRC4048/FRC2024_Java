// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.commands.deployer;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.utils.TimeoutCounter;

public class limitSwitchChange extends Command {
  private Deployer deployer;
  private Timer timeout = new Timer();
  /** Creates a new limitSwitchChange. */
  public limitSwitchChange(Deployer deployer) {
    this.deployer = deployer;
    addRequirements(deployer);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timeout.reset();
    timeout.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    deployer.setDeployerMotorSpeed(1);
    if(timeout.hasElapsed(0.1)){
      deployer.switchConfigTSRX();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    deployer.setDeployerMotorSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return !(timeout.hasElapsed(3));
  }
}
