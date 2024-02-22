package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;

public class StartIntakeAndFeeder extends SequentialCommandGroup{
    public StartIntakeAndFeeder(Feeder feeder, IntakeSubsystem intake) {
        addCommands(
            new ParallelDeadlineGroup(
                new StartFeeder(feeder),
                new StartIntake(intake, 10)
            ),
            new WaitCommand(0.1),
            new FeederBackDrive(feeder)
        );
        addRequirements(feeder,intake);

    }
}
