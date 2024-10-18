package frc.robot.subsystems.deployer;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class DeployerInputs implements LoggableInputs {

    public double deployerSpeed = 0;
    public boolean revLimit = false;
    public boolean fwdLimit = false;

    @Override
    public void toLog(LogTable table) {
        table.put("deployerSpeed", deployerSpeed);
        table.put("revLimit", revLimit);
        table.put("fwdLimit", fwdLimit);
    }

    @Override
    public void fromLog(LogTable table) {
        deployerSpeed = table.get("deployerSpeed", deployerSpeed);
        revLimit = table.get("revLimit", revLimit);
        fwdLimit = table.get("fwdLimit", fwdLimit);
    }
}
