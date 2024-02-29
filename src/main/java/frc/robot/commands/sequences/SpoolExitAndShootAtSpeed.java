package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.shooter.SetShooterSpeed;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class SpoolExitAndShootAtSpeed extends ParallelCommandGroup {
    public SpoolExitAndShootAtSpeed(Shooter shooter, Feeder feeder) {
        addCommands(
                new SetShooterSpeed(shooter),
                new SequentialCommandGroup(
                        new WaitCommand(Constants.SPOOL_TIME),
                        new FeederGamepieceUntilLeave(feeder)
                )
        );
    }
}
