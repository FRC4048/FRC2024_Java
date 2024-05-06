package frc.robot.autochooser.event;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import org.littletonrobotics.junction.Logger;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Superclass of {@link AutoEventProviderIO} that uses Network Tables to get
 * {@link AutoAction AutoActions} and {@link FieldLocation fieldLocations}<br>
 */
public class AutoEventProvider {
    private final AutoEventProviderIO providerIO;
    private final BiFunction<AutoAction, FieldLocation, Boolean> validator;
    private final AutoChooserInputs inputs = new AutoChooserInputs();

    public AutoEventProvider(AutoEventProviderIO providerIO, BiFunction<AutoAction, FieldLocation, Boolean> validator) {
        this.providerIO = providerIO;
        this.validator = validator;
        providerIO.setOnActionChangeListener(action -> {
            if (validator.apply(action, getSelectedLocation())) {
                providerIO.setFeedbackAction(action);
                providerIO.setFeedbackLocation(getSelectedLocation());
                providerIO.runValidCommands();
            } else {
                providerIO.setFeedbackAction(AutoAction.INVALID);
                providerIO.setFeedbackLocation(FieldLocation.INVALID);
            }
        });
        providerIO.setOnLocationChangeListener(autoLocation -> {
            if (validator.apply(getSelectedAction(), autoLocation)) {
                providerIO.setFeedbackAction(getSelectedAction());
                providerIO.setFeedbackLocation(autoLocation);
                providerIO.runValidCommands();
            } else {
                providerIO.setFeedbackAction(AutoAction.INVALID);
                providerIO.setFeedbackLocation(FieldLocation.INVALID);
            }
        });
    }

    public AutoAction getSelectedAction() {
        return inputs.action == null ? inputs.defaultAction : inputs.action;
    }

    public FieldLocation getSelectedLocation() {
        return inputs.location == null ? inputs.defaultLocation : inputs.location;
    }

    public void updateInputs() {
        providerIO.updateInputs(inputs);
        Logger.processInputs("AutoChooserInputs", inputs);
    }

    public void setOnActionChangeListener(Consumer<AutoAction> listener) {
        providerIO.setOnActionChangeListener(listener);
    }

    public void setOnLocationChangeListener(Consumer<FieldLocation> listener) {
        providerIO.setOnLocationChangeListener(listener);
    }
    public void forceRefresh(){
        if (validator.apply(getSelectedAction(), getSelectedLocation())) {
            providerIO.setFeedbackAction(getSelectedAction());
            providerIO.setFeedbackLocation(getSelectedLocation());
            providerIO.runValidCommands();
        }else {
            providerIO.setFeedbackAction(AutoAction.INVALID);
            providerIO.setFeedbackLocation(FieldLocation.INVALID);
        }

    }

    public void addOnValidationCommand(Callable<Command> c) {
        providerIO.addOnValidationCommand(c);
    }
}