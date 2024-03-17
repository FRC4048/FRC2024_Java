package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class ComboShot extends ParallelCommandGroup {
    public ComboShot(Shooter shooter, Feeder feeder, Ramp ramp) {
        addCommands(
                new BasicShoot(shooter,1),
                new TimedFeeder(feeder, Constants.TIMED_FEEDER_EXIT)
        );
    }


}
