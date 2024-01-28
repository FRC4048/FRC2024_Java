package frc.robot.autochooser.event;


import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;

public interface AutoEventProvider {
    AutoAction getSelectedAction();
    FieldLocation getSelectedLocation();
    AutoAction getDefaultActionOption();
    FieldLocation getDefaultLocationOption();

}
