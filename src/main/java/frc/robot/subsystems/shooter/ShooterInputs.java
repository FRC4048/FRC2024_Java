package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ShooterInputs implements LoggableInputs {
    double shooterMotorLeftRPM;
    double shooterMotorRightRPM;
    @Override
    public void toLog(LogTable table) {
        table.put("shooterMotorLeftRPM", shooterMotorLeftRPM);
        table.put("shooterMotorRightRPM", shooterMotorRightRPM);
    }

    @Override
    public void fromLog(LogTable table) {
        shooterMotorLeftRPM = table.get("shooterMotorLeftRPM", shooterMotorLeftRPM);
        shooterMotorRightRPM = table.get("shooterMotorRightRPM", shooterMotorRightRPM);
    }
}
