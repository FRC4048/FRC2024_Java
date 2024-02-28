package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.feeder.FeederBackEject;
import frc.robot.commands.intake.StartOuttake;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;

public class EjectPiece extends ParallelCommandGroup {
    public EjectPiece(Feeder feeder, IntakeSubsystem intake) {
        super(
                new FeederBackEject(feeder, 2),
                new StartOuttake(intake, 2)
        );
        addRequirements(feeder, intake);

    }
}
