package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;

public class SpoolExitAndShoot extends ParallelCommandGroup {
    public SpoolExitAndShoot(Shooter shooter, Feeder feeder, SwerveDrivetrain drivetrain) {
        addCommands(
                new ShootSpeaker(shooter, drivetrain),
                new SequentialCommandGroup(
                        new WaitCommand(Constants.SPOOL_TIME),
                        new FeederGamepieceUntilLeave(feeder)
                )
        );
    }
}