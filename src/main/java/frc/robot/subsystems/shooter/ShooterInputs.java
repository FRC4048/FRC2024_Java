package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ShooterInputs implements LoggableInputs {

    public double shooterMotorLeftRPM;
    public double shooterMotorRightRPM;
    public double shooterMotorLeftTargetRPM;
    public double shooterMotorRightTargetRPM;

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
