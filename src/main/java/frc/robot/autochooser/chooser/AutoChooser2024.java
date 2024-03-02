package frc.robot.autochooser.chooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.commands.pathplanning.FourPieceLeft;
import frc.robot.commands.pathplanning.ShootAndDrop;
import frc.robot.subsystems.*;

import java.util.Map;

public class AutoChooser2024 extends Nt4AutoValidationChooser {
    private final Map<AutoEvent, Command> commandMap;

    public AutoChooser2024(IntakeSubsystem intakeSubsystem, Shooter shooter, Feeder feeder, Deployer deployer, Ramp ramp) {
        super(AutoAction.DoNothing, FieldLocation.SpeakFront);
        commandMap = Map.ofEntries(
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.SpeakFront), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.FeederSide), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.AmpSide), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.DoNothing, FieldLocation.ZERO), new PlaceHolderCommand()),
                Map.entry(new AutoEvent(AutoAction.ShootTwoDip, FieldLocation.AmpSide), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2AndDip")).beforeStarting(new ShootAndDrop(shooter,feeder,deployer))),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.FeederSide), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossRight"))),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.AmpSide), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossLeft"))),
                Map.entry(new AutoEvent(AutoAction.ShootAndCross, FieldLocation.SpeakFront), AutoBuilder.followPath(PathPlannerPath.fromPathFile("ShootAndCrossMid"))),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.AmpSide), new FourPieceLeft(shooter,feeder,deployer)),
                Map.entry(new AutoEvent(AutoAction.ShootFour, FieldLocation.FeederSide), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot4Right"))),
                Map.entry(new AutoEvent(AutoAction.ShootTwo, FieldLocation.AmpSide), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Shoot2Left")))
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
