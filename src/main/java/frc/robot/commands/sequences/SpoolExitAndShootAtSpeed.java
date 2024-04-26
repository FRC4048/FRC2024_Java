package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.commands.shooter.SetShooterSpeed;
import frc.robot.constants.Constants;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;

public class SpoolExitAndShootAtSpeed extends ParallelCommandGroup {
    public SpoolExitAndShootAtSpeed(Shooter shooter, Feeder feeder, LightStrip lightStrip) {
        addCommands(
                new SetShooterSpeed(shooter, lightStrip),
                new SequentialCommandGroup(
                        new WaitCommand(Constants.SPOOL_TIME),
                        new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT)
                )
        );
    }
}
