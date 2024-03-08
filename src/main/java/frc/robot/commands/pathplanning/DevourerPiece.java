package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class DevourerPiece extends ParallelDeadlineGroup {
    public DevourerPiece(SwerveDrivetrain drivetrain, Vision vision, IntakeSubsystem intake, Feeder feeder) {
        super(new IntakeFeederCombo(feeder,intake),
                new SequentialCommandGroup(
                new MoveToGamepiece(drivetrain, vision),
                new Drive(drivetrain, () -> -0.12, () -> 0, () -> 0, false)// I know this will work not sure about commented command above
            )
        );
    }
}
