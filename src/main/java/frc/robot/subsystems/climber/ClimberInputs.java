package frc.robot.subsystems.climber;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ClimberInputs implements LoggableInputs {

    public double leftServoSetpoint;
    public double rightServoSetpoint;
    public double leftClimberEnc;
    public double rightClimberEnc;
    public boolean atLeftClimberLimit;
    public boolean atRightClimberLimit;

    @Override
    public void toLog(LogTable table) {
        table.put("leftServoSetpoint", leftServoSetpoint);
        table.put("rightServoSetpoint", rightServoSetpoint);
        table.put("leftClimberEnc", leftClimberEnc);
        table.put("rightClimberEnc", rightClimberEnc);
        table.put("atLeftClimberLimit", atLeftClimberLimit);
        table.put("atRightClimberLimit", atRightClimberLimit);
    }

    @Override
    public void fromLog(LogTable table) {
        leftServoSetpoint = table.get("leftServoSetpoint", leftServoSetpoint);
        rightServoSetpoint = table.get("rightServoSetpoint", rightServoSetpoint);
        leftClimberEnc = table.get("leftClimberEnc", leftClimberEnc);
        rightClimberEnc = table.get("rightClimberEnc", rightClimberEnc);
        atLeftClimberLimit = table.get("atLeftClimberLimit", atLeftClimberLimit);
        atRightClimberLimit = table.get("atRightClimberLimit", atRightClimberLimit);
    }
}
