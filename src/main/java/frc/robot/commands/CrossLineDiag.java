// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.SwerveDrivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class CrossLineDiag extends ParallelCommandGroup {
  /** Creates a new BluePodiumRightStartPosCrossLine. */
  public CrossLineDiag(SwerveDrivetrain drivetrain, Ramp ramp) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(new MoveDistance(drivetrain, (RobotContainer.isRedAlliance()?-4:4), (RobotContainer.isRedAlliance()?-4:4), 0.3), new ResetRamp(ramp));
  }
}
