package frc.robot.subsystems.intake;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class IntakeInputs implements LoggableInputs {
    public double intakeMotor1Speed;
    public double intakeMotor2Speed;
    public double intakeMotor1Current;

    @Override
    public void toLog(LogTable table) {
        table.put("intakeMotor1Speed", intakeMotor1Speed);
        table.put("intakeMotor2Speed", intakeMotor2Speed);
        table.put("intakeMotor1Current", intakeMotor1Current);
    }

    @Override
    public void fromLog(LogTable table) {
        intakeMotor1Speed = table.get("intakeMotor1Speed", intakeMotor1Speed);
        intakeMotor2Speed = table.get("intakeMotor2Speed", intakeMotor2Speed);
        intakeMotor1Current = table.get("intakeMotor1Current", intakeMotor1Current);
    }
}
