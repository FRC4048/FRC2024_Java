package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.shooter.StopShooter;
import frc.robot.subsystems.*;

/**
 * This sequence assumes that ramp and shooter are already set up for a shot,
 * and just starts the feeder to shot. Meant to be used after ShootSpeakerSetup or ShootAmpSetup.
 */
public class ShootSpeakerAmpGo extends SequentialCommandGroup {
    public ShootSpeakerAmpGo(Shooter shooter, Feeder feeder, Amp amp, Ramp ramp) {
        addCommands(
                new FeederGamepieceUntilLeave(feeder),
                new StopShooter(shooter),
                new RetractAmpSequence(ramp, amp)  // this is not needed if shooting speaker, but not sure how to implement it in a different way
        );
    }
}