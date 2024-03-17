package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.SpoolIntake;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.commands.intake.CurrentBasedIntakeFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;
import frc.robot.utils.DriveMode;

public class DevourerPiece extends SequentialCommandGroup {
    public DevourerPiece(SwerveDrivetrain drivetrain, Vision vision, IntakeSubsystem intake, Feeder feeder) {
        super(
                new ParallelRaceGroup(
                        new SequentialCommandGroup(
                                new SpoolIntake(intake, Constants.INTAKE_SPOOL_TIME),
                                new ParallelRaceGroup(
                                        new CurrentBasedIntakeFeeder(intake, feeder),
                                        new WaitCommand(5)
                                )
                        ),
                        new SequentialCommandGroup(
                                new MoveToGamepiece(drivetrain, vision),
                                new Drive(drivetrain, () -> -0.12, () -> 0, () -> 0, ()->DriveMode.ROBOT_CENTRIC).withTimeout(1)
                        )
                )
        );
    }
}
