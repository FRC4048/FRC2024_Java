package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;

public class ExitAndShoot extends ParallelCommandGroup{
    public ExitAndShoot(Shooter shooter, Feeder feeder, SwerveDrivetrain drivetrain) {
        addCommands(
            //Waits for the shooting wheels to go faster, then shoots
            new ShootSpeaker(shooter, drivetrain),
            new SequentialCommandGroup(
                new WaitCommand(.5),
                new FeederGamepieceUntilLeave(feeder)
            )
        );
        addRequirements(shooter,feeder);

    }  
}