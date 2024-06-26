package frc.robot.autochooser.chooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.commands.auto.ShootWaitMove;
import frc.robot.commands.pathplanning.ShootAndDrop;
import frc.robot.subsystems.*;

import java.util.Map;

public class AutoChooser2024 extends Nt4AutoValidationChooser {
    private final Map<AutoEvent, Command> commandMap;

    public AutoChooser2024(IntakeSubsystem intake, Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp, SwerveDrivetrain drivetrain, LightStrip lightStrip) {
        super(AutoAction.DoNothing, FieldLocation.SpeakFront);
        this.commandMap = Map.ofEntries(
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakFront), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerRight), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerLeft), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.ZERO), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.ShootTwoDip, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2AndDip")).beforeStarting(new ShootAndDrop(shooter,feeder,deployer,lightStrip))),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerRight), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossRight")).beforeStarting(new ShootAndDrop(shooter,feeder,deployer,lightStrip))),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossLeft")).beforeStarting(new ShootAndDrop(shooter,feeder,deployer,lightStrip))),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakFront), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossMid")).beforeStarting(new ShootAndDrop(shooter,feeder,deployer,lightStrip))),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerLeft), AutoBuilder.buildAuto("FourPieceLeft")),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakFront), AutoBuilder.buildAuto("FourPieceCenter")),
                Map.entry(new AutoEvent(AutoAction.Fork, FieldLocation.SpeakerRight), AutoBuilder.buildAuto("ForkAuto")),
                Map.entry(new AutoEvent(AutoAction.SmartFork, FieldLocation.SpeakerRight), AutoBuilder.buildAuto("SmartForkAuto")),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerRight), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Right"))),
                Map.entry(new AutoEvent(AutoAction.ShootTwo, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2Left")).beforeStarting(new ShootAndDrop(shooter,feeder,deployer,lightStrip))),
                Map.entry(new AutoEvent(AutoAction.SHOOT, FieldLocation.SpeakerLeft), new ShootAndDrop(shooter,feeder,deployer,lightStrip)),
                Map.entry(new AutoEvent(AutoAction.SHOOT, FieldLocation.SpeakerRight), new ShootAndDrop(shooter,feeder,deployer,lightStrip)),
                Map.entry(new AutoEvent(AutoAction.SHOOT, FieldLocation.SpeakFront), new ShootAndDrop(shooter,feeder,deployer,lightStrip)),
                Map.entry(new AutoEvent(AutoAction.ShootWaitMove, FieldLocation.SpeakerLeft), new ShootWaitMove(drivetrain, shooter, feeder, deployer, lightStrip))
        );
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
