package frc.robot.autochooser.event;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * interface that outlines the necessary methods to provide
 * {@link FieldLocation FieldLocations}  and {@link AutoAction AutoActions}
 * */
public interface AutoEventProviderIO {
    void updateInputs(AutoChooserInputs inputs);
    void setOnActionChangeListener(Consumer<AutoAction> listener);
    void setOnLocationChangeListener(Consumer<FieldLocation> listener);
    void addOnValidationCommand(Callable<Command> consumer);
    void runValidCommands();
    void setFeedbackAction(AutoAction action);
    void setFeedbackLocation(FieldLocation location);
}
