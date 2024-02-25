package frc.robot.commands.pathplanning;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class FourPieceLeft extends SequentialCommandGroup {
    public FourPieceLeft(Shooter shooter, Feeder feeder, Deployer deployer) {
        addCommands(
                new ParallelCommandGroup(
                        new BasicShoot(shooter,0.2),
                        new FeederGamepieceUntilLeave(feeder),
                        new LowerDeployer(deployer)
                ),
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Left")),
                new ParallelCommandGroup(
                        new BasicShoot(shooter,0.2),
                        new FeederGamepieceUntilLeave(feeder)
                )
        );
    }
}
