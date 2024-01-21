package frc.robot.autochooser.chooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;

import java.util.Map;

public class ExampleAutoChooser extends Nt4AutoValidationChooser {
    private final Map<AutoEvent, Command> commandMap = Map.of(
            new AutoEvent(AutoAction.DoNothing, FieldLocation.Middle), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.DoNothing, FieldLocation.Left), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.DoNothing, FieldLocation.Right), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.FigureEight, FieldLocation.Left), AutoBuilder.followPath(PathPlannerPath.fromPathFile("Figure8")),
            new AutoEvent(AutoAction.TwoPieceMoveLeft, FieldLocation.Left), new PlaceHolderCommand()
    );

    public ExampleAutoChooser() {
        super(AutoAction.DoNothing, FieldLocation.Middle);
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
