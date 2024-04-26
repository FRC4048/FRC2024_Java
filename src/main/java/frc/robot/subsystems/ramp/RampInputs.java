package frc.robot.subsystems.ramp;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class RampInputs implements LoggableInputs {

    public double rampP;
    public double rampI;
    public double rampD;
    public double rampFF;
    public double rampTargetPos;
    public double rampSpeed;
    public boolean fwdTripped;
    public boolean revTripped;
    public double encoderPosition;

    @Override
    public void toLog(LogTable table) {
        table.put("rampP", rampP);
        table.put("rampI", rampI);
        table.put("rampD", rampD);
        table.put("rampFF", rampFF);
        table.put("rampTargetPos", rampTargetPos);
        table.put("rampSpeed", rampSpeed);
        table.put("encoderPosition", encoderPosition);
        table.put("fwdTripped", fwdTripped);
        table.put("revTripped", revTripped);
    }

    @Override
    public void fromLog(LogTable table) {
        rampP = table.get("rampP", rampP);
        rampI = table.get("rampI", rampI);
        rampD = table.get("rampD", rampD);
        rampFF = table.get("rampFF", rampFF);
        rampTargetPos = table.get("rampTargetPos", rampTargetPos);
        rampSpeed = table.get("rampSpeed", rampSpeed);
        encoderPosition = table.get("encoderPosition", encoderPosition);
        fwdTripped = table.get("fwdTripped", fwdTripped);
        revTripped = table.get("revTripped", revTripped);
    }
}
