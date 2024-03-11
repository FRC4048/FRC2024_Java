package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class DevourerPiece extends SequentialCommandGroup {
    public DevourerPiece(SwerveDrivetrain drivetrain, Vision vision, IntakeSubsystem intake, Feeder feeder) {
        super(
                new ParallelRaceGroup(
                        new StartFeeder(feeder),
                        new TimedIntake(intake, 10),
                        new SequentialCommandGroup(
                                new MoveToGamepiece(drivetrain, vision),
                                new Drive(drivetrain, () -> -0.12, () -> 0, () -> 0, false).withTimeout(1)
                        )
                ),
                new WaitCommand(Constants.FEEDER_BACK_DRIVE_DELAY),
                new FeederBackDrive(feeder)
        );
    }
}
