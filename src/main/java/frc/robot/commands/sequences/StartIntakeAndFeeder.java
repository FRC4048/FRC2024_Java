package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.commands.ramp.RampMoveAndWait;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;

/**
 * Sequence to start intaking, this takes care of the lowering/raising the deployer,
 * as well as starting/stopping the intake and feeder.
 */
public class StartIntakeAndFeeder extends SequentialCommandGroup{
    public StartIntakeAndFeeder(Feeder feeder, IntakeSubsystem intake, Deployer deployer, Ramp ramp) {
        addCommands(
            new ParallelCommandGroup(
                new LowerDeployer(deployer),
                new RampMoveAndWait(ramp, () -> GameConstants.RAMP_POS_STOW)
            ),
            new ParallelRaceGroup(
                new StartIntake(intake, 10), //intake stops by ParallelRaceGroup when note in feeder
                new StartFeeder(feeder)
            ),
            new ParallelCommandGroup(
                new RaiseDeployer(deployer),
                new SequentialCommandGroup(
                        new WaitCommand(0.5),
                        new FeederBackDrive(feeder)
                )
            )
        );
    }
}
