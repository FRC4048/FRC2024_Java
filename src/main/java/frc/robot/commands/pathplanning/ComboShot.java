package frc.robot.commands.pathplanning;

import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

public class ComboShot extends LoggableParallelCommandGroup {
    public ComboShot(Shooter shooter, Feeder feeder, LightStrip lightStrip) {
        super(
                new BasicShoot(shooter, lightStrip, 1.0),
                new LoggableSequentialCommandGroup(
                        new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT)
                        // TODO: Consider stopping shooter after x seconds
                )
        );
    }
}
