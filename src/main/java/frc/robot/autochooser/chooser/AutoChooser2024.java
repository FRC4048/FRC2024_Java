package frc.robot.autochooser.chooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.autochooser.event.AutoEventProvider;
import frc.robot.autochooser.event.AutoEventProviderIO;
import frc.robot.commands.pathplanning.ShootAndDrop;
import frc.robot.commands.pathplanning.autos.FourPieceCenter;
import frc.robot.commands.pathplanning.autos.SmartForkDouble;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.limelight.Vision;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.subsystems.swervev3.SwerveDrivetrain;
import frc.robot.utils.loggingv2.LoggableCommandWrapper;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

import java.util.Map;

public class AutoChooser2024 extends SubsystemBase implements AutoChooser {
    private final Map<AutoEvent, Command> commandMap;
    private final AutoEventProvider provider;

    public AutoChooser2024(AutoEventProviderIO providerIO, SwerveDrivetrain drivetrain, Intake intake, Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp, LightStrip lightStrip, Vision vision) {
        this.provider = new AutoEventProvider(providerIO, this::isValid);
        commandMap = Map.ofEntries(
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakFront), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerRight), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerLeft), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.ZERO), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.ShootTwoDip, FieldLocation.SpeakerLeft), new LoggableSequentialCommandGroup(
                        new ShootAndDrop(shooter,feeder,deployer,lightStrip),
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2AndDip"))))
                ),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerRight), new LoggableSequentialCommandGroup(
                        new ShootAndDrop(shooter,feeder,deployer,lightStrip),
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossRight"))))
                ),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerLeft), new LoggableSequentialCommandGroup(
                                new ShootAndDrop(shooter,feeder,deployer,lightStrip),
                                LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossLeft"))))
                ),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakFront), new LoggableSequentialCommandGroup(
                                new ShootAndDrop(shooter,feeder,deployer,lightStrip),
                                LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossMid"))))
                ),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerLeft), LoggableCommandWrapper.wrap(AutoBuilder.buildAuto("FourPieceLeft"))),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakFront), new FourPieceCenter(intake, shooter, feeder, deployer, ramp, lightStrip)),
                Map.entry(new AutoEvent(AutoAction.Fork, FieldLocation.SpeakerRight), LoggableCommandWrapper.wrap(AutoBuilder.buildAuto("ForkAuto"))),
                Map.entry(new AutoEvent(AutoAction.SmartFork, FieldLocation.SpeakerRight), new SmartForkDouble(drivetrain,intake,shooter,feeder,deployer,ramp,lightStrip,vision)),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerRight), LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Right")))),
                Map.entry(new AutoEvent(AutoAction.ShootTwo, FieldLocation.SpeakerLeft), new LoggableSequentialCommandGroup(
                        new ShootAndDrop(shooter,feeder,deployer,lightStrip),
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2Left")))
                )
        ));
    }

    @Override
    public void periodic() {
        provider.updateInputs();
    }

    public Command getAutoCommand() {
        return commandMap.get(new AutoEvent(provider.getSelectedAction(), provider.getSelectedLocation()));
    }

    @Override
    public Pose2d getStartingPosition() {
        return provider.getSelectedLocation().getLocation();
    }

    protected boolean isValid(AutoAction action, FieldLocation location) {
        return commandMap.containsKey(new AutoEvent(action, location));
    }

    public AutoEventProvider getProvider(){
        return provider;
    }
}
