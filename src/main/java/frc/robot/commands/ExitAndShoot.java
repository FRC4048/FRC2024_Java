package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.cannon.Shoot;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class ExitAndShoot extends SequentialCommandGroup{
    public ExitAndShoot(Shooter shooter, Feeder feeder) {
        addCommands(new ParallelCommandGroup(
                new Shoot(shooter),
                new SequentialCommandGroup(
                        new WaitCommand(0.5),
                        new FeederGamepieceUntilLeave(feeder))
        ));
        addRequirements(shooter,feeder);

    }  
}
