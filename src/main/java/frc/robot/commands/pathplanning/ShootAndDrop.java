package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.feeder.FeederGamepieceUntilLeave;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.Shooter;

public class ShootAndDrop extends ParallelDeadlineGroup {
    public ShootAndDrop(Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp) {
        super(new BasicShoot(shooter,0.3),
                new FeederGamepieceUntilLeave(feeder,ramp),
                new LowerDeployer(deployer));
    }
}
