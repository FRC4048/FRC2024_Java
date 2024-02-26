package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.ramp.RampMove;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.commands.shooter.ShootSpeaker;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;

/**
 * This sequence should be used to set up before a speaker shot. It raises the ramp to the correct
 * height and starts spinning the shooter at the right speed and spin.
 */
public class ShootSpeakerSetup extends ParallelCommandGroup {
    public ShootSpeakerSetup(Shooter shooter, Ramp ramp, SwerveDrivetrain drivetrain, double rampValue) {
        addCommands(
                new RampMove(ramp, () -> rampValue),
                new ShootSpeaker(shooter, drivetrain)
        );
    }
}