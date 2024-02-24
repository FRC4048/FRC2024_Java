package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class ExitAndShoot extends ParallelCommandGroup{
    public ExitAndShoot(Shooter shooter, Feeder feeder) {
        addCommands(
                new ShootSpeaker(shooter,10),
                new FeederGamepieceUntilLeave(feeder)
        );
        addRequirements(shooter,feeder);

    }  
}
