package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;

public class ShootAndDrop extends ParallelDeadlineGroup {
    public ShootAndDrop(Shooter shooter, Feeder feeder, Deployer deployer, LightStrip lightStrip) {
        super(new BasicShoot(shooter, lightStrip,0.3),
                new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT),
                new LowerDeployer(deployer, lightStrip));
    }
}
