package frc.robot.autochooser.chooser;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;

import java.util.Map;

public class ExampleAutoValidationChooser extends Nt4AutoValidationChooser {
    private final Map<AutoEvent, Command> commandMap = Map.of(
            new AutoEvent(AutoAction.DoNothing, FieldLocation.Middle), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.DoNothing, FieldLocation.Left), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.DoNothing, FieldLocation.Right), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.TwoPieceMoveLeft, FieldLocation.Right), new PlaceHolderCommand(),
            new AutoEvent(AutoAction.TwoPieceMoveLeft, FieldLocation.Left), new PlaceHolderCommand()
    );

    public ExampleAutoValidationChooser() {
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
