package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.deployer.RaiseDeployer;
import frc.robot.commands.intake.StopIntake;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.IntakeSubsystem;

public class StopIntakeGroup extends ParallelCommandGroup{
    public StopIntakeGroup(Deployer deployer, IntakeSubsystem intakeSubsystem) {
        addCommands(
            new RaiseDeployer(deployer),
            new StopIntake(intakeSubsystem)
        );
        
    }
}
