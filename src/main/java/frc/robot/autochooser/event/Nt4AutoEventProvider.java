package frc.robot.autochooser.event;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Superclass of {@link AutoEventProvider} that uses Network Tables to get
 * {@link AutoAction AutoActions} and {@link FieldLocation fieldLocations}<br>
 */
public class Nt4AutoEventProvider implements AutoEventProvider {
    public static final String AUTO_TAB_NAME = "Auto";
    public static final String ACTION_FIELD_NAME = "Auto Action";
    public static final String LOCATION_FIELD_NAME = "Location Chooser";

    private final SendableChooser<AutoAction> actionChooser;
    private final SendableChooser<FieldLocation> locationChooser;

    private final AutoAction defaultAutoAction;
    private final FieldLocation defaultFieldLocation;

    public Nt4AutoEventProvider(AutoAction defaultAutoAction, FieldLocation defaultFieldLocation) {
        this.defaultAutoAction = defaultAutoAction;
        this.defaultFieldLocation = defaultFieldLocation;
        this.actionChooser = new SendableChooser<>();
        this.locationChooser = new SendableChooser<>();
        Arrays.stream(AutoAction.values()).forEach(a -> actionChooser.addOption(a.getName(), a));
        Arrays.stream(FieldLocation.values()).forEach(l -> locationChooser.addOption(l.getShuffleboardName(), l));
        actionChooser.setDefaultOption(getDefaultActionOption().getName(), getDefaultActionOption());
        locationChooser.setDefaultOption(getDefaultLocationOption().name(), getDefaultLocationOption());
        ShuffleboardTab autoTab = Shuffleboard.getTab(AUTO_TAB_NAME);
        autoTab.add(ACTION_FIELD_NAME, actionChooser).withWidget(BuiltInWidgets.kComboBoxChooser).withPosition(0, 0).withSize(4, 1);
        autoTab.add(LOCATION_FIELD_NAME, locationChooser).withPosition(0, 1).withSize(4, 1);
    }
    public AutoAction getSelectedAction() {
        return actionChooser.getSelected() == null ? getDefaultActionOption() : actionChooser.getSelected();
    }

    public FieldLocation getSelectedLocation() {
        return locationChooser.getSelected() == null ? getDefaultLocationOption() : locationChooser.getSelected();
    }

    @Override
    public AutoAction getDefaultActionOption() {
        return defaultAutoAction;
    }

    @Override
    public FieldLocation getDefaultLocationOption() {
        return defaultFieldLocation;
    }

    /**
     * @param listener function to be called when the value in {@link #actionChooser} changes
     */
    public void setOnActionChangeListener(Consumer<AutoAction> listener){
        actionChooser.onChange(listener);
    }
    /**
     * @param listener function to be called when the value in {@link #locationChooser} changes
     */
    public void setOnLocationChangeListener(Consumer<FieldLocation> listener){
        locationChooser.onChange(listener);
    }
}