package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.deployer.RaiseAndLowerDeployer;
import frc.robot.commands.feeder.FeederBackDrive;
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
                new StartAndStopIntake(intake, deployer),
                new StartAndStopFeeder(intake, feeder, deployer)
            ),
            new WaitCommand(0.5),
            new FeederBackDrive(feeder)
        );
    }
    
}
