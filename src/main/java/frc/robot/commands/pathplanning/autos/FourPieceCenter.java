package frc.robot.commands.pathplanning.autos;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import frc.robot.commands.pathplanning.ComboShot;
import frc.robot.commands.pathplanning.RampShootCombo;
import frc.robot.commands.pathplanning.ShootAndDrop;
import frc.robot.commands.pathplanning.SlurpWithRamp;
import frc.robot.constants.Constants;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableCommandWrapper;
import frc.robot.utils.loggingv2.LoggableDeadlineCommandGroup;
import frc.robot.utils.loggingv2.LoggableParallelCommandGroup;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

public class FourPieceCenter extends LoggableSequentialCommandGroup {
    public FourPieceCenter(Intake intake, Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp, LightStrip lightStrip) {
        super(
                new ShootAndDrop(shooter,feeder,deployer,lightStrip),
                new LoggableParallelCommandGroup(
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("InitCenter"))).withBasicName("FollowInitCenter"),
                        new SlurpWithRamp(intake,feeder,lightStrip,ramp)
                ),
                new LoggableDeadlineCommandGroup(
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("CenterShootRight"))).withBasicName("FollowCenterShootRight"),
                        new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_SIDE_AUTO_SHOOT).withBasicName("SpeakerFront/RampShootComboSide")
                ),
                new ComboShot(shooter, feeder, lightStrip),
                new LoggableParallelCommandGroup(
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("RightShotPick"))).withBasicName("FollowRightShotPick"),
                        new SlurpWithRamp(intake,feeder,lightStrip,ramp)
                ),
                new LoggableDeadlineCommandGroup(
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("StrafeL2C"))).withBasicName("FollowStrafeL2C"),
                        new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_CENTER_AUTO_SHOOT).withBasicName("RampShootComboCenter")
                ),
                new ComboShot(shooter, feeder, lightStrip),
                new LoggableParallelCommandGroup(
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("Center2Right"))).withBasicName("FollowCenter2Right"),
                        new SlurpWithRamp(intake,feeder,lightStrip,ramp)
                ),
                new LoggableDeadlineCommandGroup(
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootRightCenter"))).withBasicName("FollowShootRightCenter"),
                        new RampShootCombo(ramp, shooter, lightStrip, Constants.RAMP_SIDE_AUTO_SHOOT).withBasicName("RampShootComboSide")
                ),
                new ShootAndDrop(shooter, feeder, deployer, lightStrip)
        );
    }
}
