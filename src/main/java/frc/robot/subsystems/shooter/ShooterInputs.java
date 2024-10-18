package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ShooterInputs implements LoggableInputs {

    public double shooterMotorLeftRPM = 0;
    public double shooterMotorRightRPM = 0;
    public double shooterMotorLeftTargetRPM = 0;
    public double shooterMotorRightTargetRPM = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("shooterMotorLeftRPM", shooterMotorLeftRPM);
        table.put("shooterMotorRightRPM", shooterMotorRightRPM);
        table.put("shooterMotorLeftTargetRPM", shooterMotorLeftTargetRPM);
        table.put("shooterMotorRightTargetRPM", shooterMotorRightTargetRPM);
    }

    @Override
    public void fromLog(LogTable table) {
        shooterMotorLeftRPM = table.get("shooterMotorLeftRPM", shooterMotorLeftRPM);
        shooterMotorRightRPM = table.get("shooterMotorRightRPM", shooterMotorRightRPM);
        shooterMotorLeftTargetRPM = table.get("shooterMotorLeftTargetRPM", shooterMotorLeftTargetRPM);
        shooterMotorRightTargetRPM = table.get("shooterMotorRightTargetRPM", shooterMotorRightTargetRPM);
    }
}
