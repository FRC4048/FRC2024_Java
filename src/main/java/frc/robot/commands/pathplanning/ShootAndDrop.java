package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Shooter;

public class ShootAndDrop extends ParallelCommandGroup {
    public ShootAndDrop(Shooter shooter, Feeder feeder, Deployer deployer) {
        addCommands(new BasicShoot(shooter,0.3),
                new FeederGamepieceUntilLeave(feeder),
                new LowerDeployer(deployer));
    }
}
