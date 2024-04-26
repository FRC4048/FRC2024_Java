package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class IntakeInputs implements LoggableInputs {
    public double intakeMotor1Current;

    @Override
    public void toLog(LogTable table) {
        table.put("intakeMotor1Current", intakeMotor1Current);
    }

    @Override
    public void fromLog(LogTable table) {
        intakeMotor1Current = table.get("intakeMotor1Current", intakeMotor1Current);
    }
}
