package frc.robot.commands.pathplanning;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableCommandWrapper;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

public class FourPieceLeft extends LoggableSequentialCommandGroup {
    public FourPieceLeft(Shooter shooter, Feeder feeder, Deployer deployer, LightStrip lightStrip) {
       super(
               new ShootAndDrop(shooter,feeder,deployer, lightStrip),
               LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Left"))),
               new ShootAndDrop(shooter,feeder,deployer, lightStrip)
       );
    }
}
