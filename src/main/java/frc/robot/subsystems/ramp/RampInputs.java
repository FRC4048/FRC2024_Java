package frc.robot.subsystems.ramp;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class RampInputs implements LoggableInputs {

    public double rampP = 0;
    public double rampI = 0;
    public double rampD = 0;
    public double rampFF = 0;
    public double rampTargetPos = 0;
    public boolean fwdTripped = false;
    public boolean revTripped = false;
    public double encoderPosition = 0;
    public double setSpeed = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("rampP", rampP);
        table.put("rampI", rampI);
        table.put("rampD", rampD);
        table.put("rampFF", rampFF);
        table.put("rampTargetPos", rampTargetPos);
        table.put("encoderPosition", encoderPosition);
        table.put("setSpeed", setSpeed);
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
        encoderPosition = table.get("encoderPosition", encoderPosition);
        setSpeed = table.get("setSpeed", encoderPosition);
        fwdTripped = table.get("fwdTripped", fwdTripped);
        revTripped = table.get("revTripped", revTripped);
    }
}
