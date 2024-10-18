package frc.robot.commands.sequences;

import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.commands.shooter.SetShooterSpeed;
import frc.robot.constants.Constants;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;
import frc.robot.utils.loggingv2.LoggableWaitCommand;

public class SpoolExitAndShootAtSpeed extends LoggableParallelCommandGroup {
    public SpoolExitAndShootAtSpeed(Shooter shooter, Feeder feeder, LightStrip lightStrip) {
        super(
                new SetShooterSpeed(shooter, lightStrip),
                new LoggableSequentialCommandGroup(
                        new LoggableWaitCommand(Constants.SPOOL_TIME),
                        new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT)
                )
        );
    }
}
