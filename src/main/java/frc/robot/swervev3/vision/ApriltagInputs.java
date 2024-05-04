package frc.robot.swervev3.vision;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class ApriltagInputs implements LoggableInputs {
    public double[] timestamp;
    public double[] serverTime;
    public double[] posX;
    public double[] posY;
    public double[] rotationDeg;
    public int[] apriltagNumber;

    @Override
    public void toLog(LogTable table) {
        table.put("timestamp", timestamp);
        table.put("serverTime", serverTime);
        table.put("posX", posX);
        table.put("posY", posY);
        table.put("rotationDeg", rotationDeg);
        table.put("apriltagNumber", apriltagNumber);
    }

    @Override
    public void fromLog(LogTable table) {
        timestamp = table.get("timestamp", timestamp);
        serverTime = table.get("serverTime", serverTime);
        posX = table.get("posX", posX);
        posY = table.get("posY", posY);
        rotationDeg = table.get("rotationDeg", rotationDeg);
        apriltagNumber = table.get("apriltagNumber", apriltagNumber);
    }
}
