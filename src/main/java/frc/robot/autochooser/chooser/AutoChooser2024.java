package frc.robot.autochooser.chooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;

import java.util.Map;

public class AutoChooser2024 extends Nt4AutoValidationChooser {
    private final Map<AutoEvent, Command> commandMap;

    public AutoChooser2024() {
        super(AutoAction.DoNothing, FieldLocation.SpeakFront);
        commandMap = Map.of(
                new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakFront), new PlaceHolderCommand(),
                new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerRight), new PlaceHolderCommand(),
                new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakerLeft), new PlaceHolderCommand(),
                new AutoEvent(AutoAction.DoNothing, FieldLocation.ZERO), new PlaceHolderCommand(),
                new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerRight), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossRight")),
                new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossLeft")),
                new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakFront), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossMid")),
                new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Left")),
                new AutoEvent(AutoAction.ShootFour, FieldLocation.SpeakerRight), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Right")),
                new AutoEvent(AutoAction.ShootTwo, FieldLocation.SpeakerLeft), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2Left"))
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
