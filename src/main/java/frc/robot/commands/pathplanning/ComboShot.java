package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.commands.shooter.StopShooter;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;

public class ComboShot extends ParallelCommandGroup {
    public ComboShot(Shooter shooter, Feeder feeder, LightStrip lightStrip) {
        addCommands(
                new BasicShoot(shooter, lightStrip,1),
                new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT),
                new StopShooter(shooter)
        );
    }


}
