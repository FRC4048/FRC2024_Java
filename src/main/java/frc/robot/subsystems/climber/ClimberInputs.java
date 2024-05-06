package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ClimberInputs implements LoggableInputs {

    public double leftServoSetpoint = 0;
    public double rightServoSetpoint = 0;
    public double leftClimberEnc = 0;
    public double rightClimberEnc = 0;
    public double leftTargetSpeed = 0;
    public double rightTargetSpeed = 0;
    public boolean atLeftClimberLimit = false;
    public boolean atRightClimberLimit = false;

    @Override
    public void toLog(LogTable table) {
        table.put("leftServoSetpoint", leftServoSetpoint);
        table.put("rightServoSetpoint", rightServoSetpoint);
        table.put("leftClimberEnc", leftClimberEnc);
        table.put("rightClimberEnc", rightClimberEnc);
        table.put("leftTargetSpeed", leftTargetSpeed);
        table.put("rightTargetSpeed", rightTargetSpeed);
        table.put("atLeftClimberLimit", atLeftClimberLimit);
        table.put("atRightClimberLimit", atRightClimberLimit);
    }

    @Override
    public void fromLog(LogTable table) {
        leftServoSetpoint = table.get("leftServoSetpoint", leftServoSetpoint);
        rightServoSetpoint = table.get("rightServoSetpoint", rightServoSetpoint);
        leftClimberEnc = table.get("leftClimberEnc", leftClimberEnc);
        rightClimberEnc = table.get("rightClimberEnc", rightClimberEnc);
        leftTargetSpeed = table.get("leftTargetSpeed", leftTargetSpeed);
        rightTargetSpeed = table.get("rightTargetSpeed", rightTargetSpeed);
        atLeftClimberLimit = table.get("atLeftClimberLimit", atLeftClimberLimit);
        atRightClimberLimit = table.get("atRightClimberLimit", atRightClimberLimit);
    }
}
