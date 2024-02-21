// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.RobotContainer;
import frc.robot.commands.cannon.Shoot;
import frc.robot.commands.cannon.StartIntake;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class shootcrossline extends SequentialCommandGroup {
  /** Creates a new shootcrossline. */
  public shootcrossline(SwerveDrivetrain SwerveDrivetrain, Shooter Shooter, Ramp ramp, IntakeSubsystem IntakeSubsystem) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ResetRamp(ramp),
      new StartIntake(IntakeSubsystem, 6), // adjust later
      new MoveDistance(SwerveDrivetrain, (RobotContainer.isRedAlliance()?-2.9718:2.9718), 0, 0.3),
      new Shoot(Shooter),
      new MoveDistance(SwerveDrivetrain, (RobotContainer.isRedAlliance()?-1.0282:1.0282), 0, 0.3)
    );
  }
}
