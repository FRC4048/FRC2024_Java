package frc.robot.autochooser.event;

import frc.robot.autochooser.AutoAction;
import frc.robot.autochooser.FieldLocation;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class AutoChooserInputs implements LoggableInputs {
    AutoAction action;
    FieldLocation location;
    AutoAction defaultAction;
    FieldLocation defaultLocation;
    AutoAction feedbackAction;
    FieldLocation feedbackLocation;

    @Override
    public void toLog(LogTable table) {
        table.put("action", action);
        table.put("location", location);
        table.put("defaultAction", defaultAction);
        table.put("defaultLocation", defaultLocation);
        table.put("feedbackAction", feedbackAction);
        table.put("feedbackLocation", feedbackLocation);
    }

    @Override
    public void fromLog(LogTable table) {
        action = table.get("action", action);
        location = table.get("location", location);
        defaultAction = table.get("defaultAction", defaultAction);
        defaultLocation = table.get("defaultLocation", defaultLocation);
        feedbackAction = table.get("feedbackAction", feedbackAction);
        feedbackLocation = table.get("feedbackLocation", feedbackLocation);
    }
}
