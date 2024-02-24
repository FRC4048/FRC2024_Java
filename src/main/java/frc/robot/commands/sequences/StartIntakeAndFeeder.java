package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.feeder.StartFeeder;
import frc.robot.commands.intake.StartIntake;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.feeder.FeederBackDrive;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;

public class StartIntakeAndFeeder extends SequentialCommandGroup{
    public StartIntakeAndFeeder(Feeder feeder, IntakeSubsystem intake, Deployer deployer, Ramp ramp) {
        addCommands(
           new ParallelCommandGroup(
               new LowerDeployer(deployer),
               new ResetRamp(ramp)
           ),
            new ParallelRaceGroup(
                new StartIntake(intake, 10),
                new StartFeeder(feeder)
            ),
            new WaitCommand(0.5),
            new FeederBackDrive(feeder)
        );
        addRequirements(feeder,intake,deployer,ramp);

    }
}
