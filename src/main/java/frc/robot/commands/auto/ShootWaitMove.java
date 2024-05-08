package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.drivetrain.MoveDistance;
import frc.robot.commands.pathplanning.ShootAndDrop;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.swervev3.SwerveDrivetrain;

public class ShootWaitMove extends SequentialCommandGroup {
    
    public ShootWaitMove(SwerveDrivetrain drivetrain, Shooter shooter, Feeder feeder, Deployer deployer, LightStrip lightstrip) {
        super(
            new ShootAndDrop(shooter, feeder, deployer, lightstrip),
            new WaitCommand(10.0),
            new MoveDistance(drivetrain, lightstrip, 2.0, 0, 0.4)
        );
    }
}
