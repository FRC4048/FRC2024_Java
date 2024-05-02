package frc.robot.commands.pathplanning.autos;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import frc.robot.commands.pathplanning.ComboShot;
import frc.robot.commands.pathplanning.DevourerPiece;
import frc.robot.commands.pathplanning.RampShootCombo;
import frc.robot.commands.pathplanning.ShootAndDrop;
import frc.robot.commands.ramp.ResetRamp;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utils.loggingv2.LoggableCommandWrapper;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

public class SmartForkSingle extends LoggableSequentialCommandGroup {
    public SmartForkSingle(SwerveDrivetrain drivetrain, Intake intake, Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp, LightStrip lightStrip, Vision vision) {
        super(
                new ShootAndDrop(shooter,feeder,deployer,lightStrip),
                new LoggableParallelCommandGroup(
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("DipRight"))).withBasicName("FollowDipRight"),
                        new ResetRamp(ramp, lightStrip)
                ),
                new DevourerPiece(drivetrain,vision, intake,feeder,deployer,lightStrip),
                new LoggableParallelCommandGroup(
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("DipRight"))).withBasicName("FollowDipRight"),
                        new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_DIP_AUTO_SHOOT).withBasicName("RampShootComboSide2")
                ),
                new ComboShot(shooter, feeder,lightStrip)
        );
    }
}
