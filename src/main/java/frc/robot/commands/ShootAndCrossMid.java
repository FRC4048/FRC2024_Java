package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;

public class ShootAndCrossMid extends SequentialCommandGroup{
     public ShootAndCrossMid(SwerveDrivetrain swerveDrivetrain, Shooter shooter, Ramp ramp, IntakeSubsystem intakeSubsystem) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ResetRamp(ramp),
      new MoveDistance(swerveDrivetrain, (RobotContainer.isRedAlliance() ? -1.015 : 1.015), 0, 0.3),
      new RampMove(ramp, ()->30), //refine later
      new WaitCommand(0.05), //refine later
      new ShootSpeaker(shooter),
      new ResetRamp(ramp)
    );
  }
}
