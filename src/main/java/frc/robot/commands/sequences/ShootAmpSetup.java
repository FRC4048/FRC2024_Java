package frc.robot.commands.sequences;

        import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
        import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
        import frc.robot.commands.ramp.RampMove;
        import frc.robot.commands.shooter.ShootAmp;
        import frc.robot.constants.GameConstants;
        import frc.robot.subsystems.Amp;
        import frc.robot.subsystems.Ramp;
        import frc.robot.subsystems.Shooter;

/**
 * This sequence should be used to set up before the amp shot. It raises the ramp to the correct
 * height, deploys the amp attachment and starts spinning the shooter at the right speed and spin.
 */
public class ShootAmpSetup extends SequentialCommandGroup {
    public ShootAmpSetup(Shooter shooter, Ramp ramp, Amp amp) {
        addCommands(
                new DeployAmpSequence(ramp, amp),
                new ParallelCommandGroup(
                        new RampMove(ramp, () -> GameConstants.RAMP_POS_SHOOT_AMP),
                        new ShootAmp(shooter)
                )
        );
    }
}