package frc.robot.autochooser.chooser;

import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.autochooser.event.Nt4AutoEventProvider;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public abstract class Nt4AutoValidationChooser extends AutoChooser {
    public static final String AUTO_LOCATION_FEEDBACK_NAME = "FieldLocationFeedback";
    public static final String AUTO_ACTION_FEEDBACK_NAME = "AutoActionFeedback";

    public Nt4AutoValidationChooser(AutoAction defaultAction, FieldLocation defaultLocation) {
        super(new Nt4AutoEventProvider(defaultAction, defaultLocation));
        setupListeners();
    }

    private void setupListeners() {
        Nt4AutoEventProvider provider = (Nt4AutoEventProvider) getProvider();
        SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_ACTION_FEEDBACK_NAME, provider.getDefaultActionOption().toString())
                .withPosition(2,2).withSize(2,1);
        SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_LOCATION_FEEDBACK_NAME, provider.getDefaultLocationOption().toString())
                .withPosition(0,2).withSize(2,1);
        provider.setOnActionChangeListener(action -> {
            if (isValid(action, provider.getSelectedLocation())) {
                SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_ACTION_FEEDBACK_NAME, action.toString());
                SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_LOCATION_FEEDBACK_NAME, provider.getSelectedLocation().toString());
            } else {
                SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_ACTION_FEEDBACK_NAME, "INVALID");
                SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_LOCATION_FEEDBACK_NAME, "INVALID");
            }
        });
        provider.setOnLocationChangeListener(autoLocation -> {
            if (isValid(provider.getSelectedAction(), autoLocation)) {
                SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_LOCATION_FEEDBACK_NAME, autoLocation.toString());
                SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_ACTION_FEEDBACK_NAME, provider.getSelectedAction().toString());
            } else {
                SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_ACTION_FEEDBACK_NAME, "INVALID");
                SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME, AUTO_LOCATION_FEEDBACK_NAME, "INVALID");
            }
        });
    }
    public void forceRefresh(){
        SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME,AUTO_LOCATION_FEEDBACK_NAME,getProvider().getSelectedLocation().toString());
        SmartShuffleboard.put(Nt4AutoEventProvider.AUTO_TAB_NAME,AUTO_ACTION_FEEDBACK_NAME,getProvider().getSelectedAction().toString());
    }

    protected abstract boolean isValid(AutoAction action, FieldLocation location);

}