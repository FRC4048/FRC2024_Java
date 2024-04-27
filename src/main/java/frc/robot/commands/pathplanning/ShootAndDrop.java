package frc.robot.commands.pathplanning;

import frc.robot.commands.deployer.LowerDeployer;
import frc.robot.commands.feeder.TimedFeeder;
import frc.robot.constants.Constants;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableDeadlineCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;
import frc.robot.utils.loggingv2.LoggableWaitCommand;

public class ShootAndDrop extends LoggableDeadlineCommandGroup {
    public ShootAndDrop(Shooter shooter, Feeder feeder, Deployer deployer, LightStrip lightStrip) {
        super(new BasicShoot(shooter, lightStrip, 1.0),
                new LoggableSequentialCommandGroup(
                        new LoggableWaitCommand(0.5),
                        new LoggableSequentialCommandGroup(
                            new TimedFeeder(feeder, lightStrip, Constants.TIMED_FEEDER_EXIT)
                            // TODO: Consider stopping shooter after X seconds
                        )
                ),
                new LowerDeployer(deployer, lightStrip));
    }
}
