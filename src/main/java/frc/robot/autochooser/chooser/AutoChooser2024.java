package frc.robot.autochooser.chooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.commands.pathplanning.ShootAndDrop;
import frc.robot.commands.pathplanning.autos.FourPieceCenter;
import frc.robot.subsystems.deployer.Deployer;
import frc.robot.subsystems.feeder.Feeder;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.loggingv2.LoggableCommandWrapper;
import frc.robot.utils.loggingv2.LoggableSequentialCommandGroup;

import java.util.Map;

public class AutoChooser2024 extends Nt4AutoValidationChooser {
    private final Map<AutoEvent, Command> commandMap;

    public AutoChooser2024(Intake intake, Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp, LightStrip lightStrip) {
        super(AutoAction.DoNothing, FieldLocation.SpeakFront);
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
                Map.entry(new AutoEvent(AutoAction.SmartFork, FieldLocation.SpeakerRight), LoggableCommandWrapper.wrap(AutoBuilder.buildAuto("SmartForkAuto"))),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerRight), LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Right")))),
                Map.entry(new AutoEvent(AutoAction.ShootTwo, FieldLocation.SpeakerLeft), new LoggableSequentialCommandGroup(
                        new ShootAndDrop(shooter,feeder,deployer,lightStrip),
                        LoggableCommandWrapper.wrap(AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2Left")))
                )
        ));
    }


    @Override
    public Command getAutoCommand() {
        return commandMap.get(new AutoEvent(getProvider().getSelectedAction(), getProvider().getSelectedLocation()));
    }

    @Override
    protected boolean isValid(AutoAction action, FieldLocation location) {
        return commandMap.containsKey(new AutoEvent(action, location));
    }
}
