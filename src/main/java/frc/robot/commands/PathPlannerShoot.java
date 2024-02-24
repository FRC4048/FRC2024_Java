package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.sequences.StartIntakeAndFeeder;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class PathPlannerShoot extends ParallelCommandGroup {
    public PathPlannerShoot(Shooter shooter, Feeder feeder, Ramp ramp, IntakeSubsystem intake) {
        addCommands(
                new ShootSpeaker(shooter, 5),
                new SequentialCommandGroup(
                        new FeederGamepieceUntilLeave(feeder),
                        new WaitCommand(0.2),
                        new ParallelCommandGroup (
                                new StartIntakeAndFeeder(feeder, intake),
                                new ResetRamp(ramp)
                        ), new RampMove(ramp, ()-> 6)

                )
        );
    }
}
