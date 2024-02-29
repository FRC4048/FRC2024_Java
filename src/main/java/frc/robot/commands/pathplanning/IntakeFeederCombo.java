package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeFeederCombo extends SequentialCommandGroup {
    public IntakeFeederCombo(Feeder feeder, IntakeSubsystem intake) {
        addCommands(
                new ParallelDeadlineGroup(
                        new StartFeeder(feeder),
                        new TimedIntake(intake, 10)
                ),
                new WaitCommand(Constants.FEEDER_BACK_DRIVE_DELAY),
                new FeederBackDrive(feeder)
        );
        addRequirements(feeder,intake);

    }
}
