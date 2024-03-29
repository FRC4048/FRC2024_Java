package frc.robot.commands.pathplanning;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;

public class FourPieceLeft extends SequentialCommandGroup {
    public FourPieceLeft(Shooter shooter, Feeder feeder, Deployer deployer, LightStrip lightStrip) {
        addCommands(
                new ShootAndDrop(shooter,feeder,deployer, lightStrip),
                AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Left")),
                new ShootAndDrop(shooter,feeder,deployer, lightStrip)
        );
    }
}
