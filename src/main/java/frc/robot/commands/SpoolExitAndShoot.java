package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.cannon.ShootSpeaker;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class SpoolExitAndShoot extends ParallelCommandGroup {
    public SpoolExitAndShoot(Shooter shooter, Feeder feeder) {
        addCommands(
                new ShootSpeaker(shooter),
                new SequentialCommandGroup(
                        new WaitCommand(Constants.SPOOL_TIME),
                        new FeederGamepieceUntilLeave(feeder)
                )
        );
        addRequirements(shooter, feeder);

    }
}
