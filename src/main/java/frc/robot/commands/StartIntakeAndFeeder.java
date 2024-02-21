package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.cannon.StartFeeder;
import frc.robot.commands.cannon.StartIntake;
import frc.robot.commands.deployer.DeployerRaise;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;

public class StartIntakeAndFeeder extends SequentialCommandGroup{
    public StartIntakeAndFeeder(Feeder feeder, IntakeSubsystem intake, Deployer deployer, Ramp ramp) {
        addCommands(
            new ParallelCommandGroup(
                new DeployerRaise(deployer)
//                new ResetRamp(ramp)
            ),
            new ParallelRaceGroup(
                new StartIntake(intake, 10),
                new StartFeeder(feeder)
            )
        );
        addRequirements(feeder,intake,deployer,ramp);

    }
}
