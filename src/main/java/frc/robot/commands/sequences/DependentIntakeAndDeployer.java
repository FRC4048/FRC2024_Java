package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.deployer.RaiseAndLowerDeployer;
import frc.robot.commands.feeder.StartAndStopFeeder;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.intake.StartAndStopIntake;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;

public class DependentIntakeAndDeployer extends SequentialCommandGroup {
    public DependentIntakeAndDeployer(IntakeSubsystem intake, Deployer deployer, Feeder feeder) {
        addCommands(
            new RaiseAndLowerDeployer(deployer),
            new ParallelRaceGroup(
                new StartAndStopIntake(intake),
                new StartAndStopFeeder(intake, feeder, deployer)
            )
        );
    }
    
}
