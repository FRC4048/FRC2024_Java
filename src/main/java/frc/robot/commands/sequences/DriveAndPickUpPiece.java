package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.TurnToGamepiece;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.Vision;

public class DriveAndPickUpPiece extends SequentialCommandGroup {
    public DriveAndPickUpPiece(SwerveDrivetrain drivetrain, IntakeSubsystem intake, Ramp ramp, Deployer deployer, Feeder feeder, Vision vision) {
        addCommands(
            new ParallelCommandGroup(
                new LowerDeployer(deployer),
                new StartIntakeAndFeeder(feeder, intake, deployer, ramp)
            ),
            new TurnToGamepiece(drivetrain, vision)
        );
    }
}
