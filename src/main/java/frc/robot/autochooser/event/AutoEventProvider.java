package frc.robot.autochooser.event;


import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;

/**
 * interface that outlines the necessary methods to provide
 * {@link FieldLocation FieldLocations}  and {@link AutoAction AutoActions}
 * */
public interface AutoEventProvider {
    AutoAction getSelectedAction();
    FieldLocation getSelectedLocation();
    AutoAction getDefaultActionOption();
    FieldLocation getDefaultLocationOption();

}
