package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class TurnToGampieceGroup extends SequentialCommandGroup{
    public TurnToGampieceGroup(Vision vision, SwerveDrivetrain drivetrain, Feeder feeder, IntakeSubsystem intake, Deployer deployer, Ramp ramp) {
        addCommands(
            new MoveToGamepiece(drivetrain, vision),
            new ParallelRaceGroup(
                new StartIntakeAndFeeder(feeder, intake, deployer, ramp),
                new SequentialCommandGroup(
                    new WaitCommand(.5),
                    new Drive(drivetrain, ()-> -0.2, ()->0, ()->0, false)
                )
               
            )
           
        );
        
    }
    
}
