// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ShootAndCross extends SequentialCommandGroup {
  /** Creates a new shootcrossline. */
  public ShootAndCross(SwerveDrivetrain swerveDrivetrain, Shooter shooter, Ramp ramp, IntakeSubsystem intakeSubsystem) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ResetRamp(ramp),
      new MoveDistance(swerveDrivetrain, (RobotContainer.isRedAlliance() ? -1.933 : 1.933), 0, 0.3),
      new RampMove(ramp, ()->30), //refine later
      new WaitCommand(0.05), //refine later
      new ShootSpeaker(shooter)
      
      /*new ParallelCommandGroup(
        new ResetRamp(ramp),
        new MoveDistance(swerveDrivetrain, (RobotContainer.isRedAlliance() ? -1.0282 : 1.0282), 0, 0.3))*/
    );
  }
}
