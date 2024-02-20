package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.cannon.Shoot;
import frc.robot.commands.cannon.StartIntake;
import frc.robot.commands.deployer.DeployerLower;
import frc.robot.commands.feeder.FeederColorMatcher;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class StartIntakeAndFeeder extends SequentialCommandGroup{
    public StartIntakeAndFeeder(Feeder feeder, IntakeSubsystem intake, Deployer deployer, Ramp ramp, Shooter shooter) {
        addCommands(
            new ParallelCommandGroup(
                new DeployerLower(deployer),
                new ResetRamp(ramp)
            ),
            new ParallelRaceGroup(
                new StartIntake(intake, 10),
                new FeederColorMatcher(feeder)
            )
        );
    }
}
