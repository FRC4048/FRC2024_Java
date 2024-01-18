package frc.robot.autochooser.chooser;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.PlaceHolderCommand;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.autochooser.event.Nt4AutoEventProvider;

import java.util.Map;

public class ExampleAutoChooser extends AutoChooser {
     private final Map<AutoEvent,Command> commandMap = Map.of(
             new AutoEvent(AutoAction.TwoPieceMoveLeft, FieldLocation.Right), new PlaceHolderCommand(),
             new AutoEvent(AutoAction.TwoPieceMoveLeft,FieldLocation.Left), new PlaceHolderCommand()
     );

     public ExampleAutoChooser() {
          super(new Nt4AutoEventProvider(AutoAction.DoNothing, FieldLocation.Middle));
     }


     @Override
     public Command getAutoCommand() {
          return commandMap.get(new AutoEvent(getProvider().getSelectedAction(),getProvider().getSelectedLocation()));
     }
}
