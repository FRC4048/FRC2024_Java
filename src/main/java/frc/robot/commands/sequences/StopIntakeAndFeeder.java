package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.feeder.StopFeeder;
import frc.robot.commands.intake.StopIntake;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.IntakeSubsystem;

/**
 * Sequence to stop intaking, this takes care of the stopping the intake and feeder, and raising the deployer
 */
public class StopIntakeAndFeeder extends ParallelCommandGroup {
    public StopIntakeAndFeeder(Feeder feeder, IntakeSubsystem intake, Deployer deployer) {
        addCommands(
                new RaiseDeployer(deployer),
                new StopIntake(intake),
                new StopFeeder(feeder)
        );
    }
}
