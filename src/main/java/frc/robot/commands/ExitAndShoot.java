package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.cannon.Shoot;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class ExitAndShoot extends SequentialCommandGroup{
    public ExitAndShoot(Shooter shooter, Feeder feeder) {
        new ParallelCommandGroup(
            new FeederGamepieceUntilLeave(feeder),
            new Shoot(shooter)
        );
    }  
}
