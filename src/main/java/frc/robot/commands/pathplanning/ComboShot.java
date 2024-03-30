package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;

public class ComboShot extends ParallelCommandGroup {
    public ComboShot(Shooter shooter, Feeder feeder, LightStrip lightStrip) {
        addCommands(
                new BasicShoot(shooter, lightStrip, 1.7),
                new SequentialCommandGroup(
                    new WaitCommand(.5),
                    new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT)
                )
        );
    }


}
