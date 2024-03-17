package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.MoveToGamepiece;
import frc.robot.commands.drivetrain.Drive;
import frc.robot.subsystems.*;
import frc.robot.utils.DriveMode;

public class TurnToGampieceGroup extends ParallelRaceGroup {
    public TurnToGampieceGroup(Vision vision, SwerveDrivetrain drivetrain, Feeder feeder, IntakeSubsystem intake, Deployer deployer, Ramp ramp, LightStrip lightStrip) {
        addCommands(
            new StartIntakeAndFeeder(feeder, intake, deployer, ramp, lightStrip),
            new SequentialCommandGroup(
                new MoveToGamepiece(drivetrain, vision),
                new Drive(drivetrain, () -> -0.15, () -> 0, () -> 0, ()->DriveMode.ROBOT_CENTRIC)
            )

        );

    }

}
