package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;

public class ComboShot extends ParallelCommandGroup {
    public ComboShot(Shooter shooter, Feeder feeder, LightStrip lightStrip) {
        addCommands(
                new BasicShoot(shooter, lightStrip, 1.0),
                new SequentialCommandGroup(
                    new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT)
                    // TODO: Consider stopping shooter after x seconds
                )
        );
    }
}
