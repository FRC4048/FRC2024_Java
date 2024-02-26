package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class ComboShot extends ParallelCommandGroup {
    public ComboShot(Shooter shooter, Feeder feeder) {
        addCommands(
                new BasicShoot(shooter,1),
                new FeederGamepieceUntilLeave(feeder)
        );
        addRequirements(shooter,feeder);
    }


}
