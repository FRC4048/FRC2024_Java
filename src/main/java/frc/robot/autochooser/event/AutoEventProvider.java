package frc.robot.autochooser.event;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import frc.robot.subsystems.LoggableSystem;

import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Superclass of {@link AutoEventProviderIO} that uses Network Tables to get
 * {@link AutoAction AutoActions} and {@link FieldLocation fieldLocations}<br>
 */
public class AutoEventProvider {
    private final LoggableSystem<AutoEventProviderIO, AutoChooserInputs> system;
    private final BiFunction<AutoAction, FieldLocation, Boolean> validator;

    public AutoEventProvider(AutoEventProviderIO providerIO, BiFunction<AutoAction, FieldLocation, Boolean> validator) {
        this.system = new LoggableSystem<>(providerIO, new AutoChooserInputs());
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
        return system.getInputs().action == null ? system.getInputs().defaultAction : system.getInputs().action;
    }

    public FieldLocation getSelectedLocation() {
        return system.getInputs().location == null ? system.getInputs().defaultLocation : system.getInputs().location;
    }

    public void updateInputs() {
        system.updateInputs();
    }

    public void setOnActionChangeListener(Consumer<AutoAction> listener) {
        system.getIO().setOnActionChangeListener(listener);
    }

    public void setOnLocationChangeListener(Consumer<FieldLocation> listener) {
        system.getIO().setOnLocationChangeListener(listener);
    }
    public void forceRefresh(){
        if (validator.apply(getSelectedAction(), getSelectedLocation())) {
            system.getIO().setFeedbackAction(getSelectedAction());
            system.getIO().setFeedbackLocation(getSelectedLocation());
            system.getIO().runValidCommands();
        }else {
            system.getIO().setFeedbackAction(AutoAction.INVALID);
            system.getIO().setFeedbackLocation(FieldLocation.INVALID);
        }

    }

    public void addOnValidationCommand(Callable<Command> c) {
        system.getIO().addOnValidationCommand(c);
    }
}