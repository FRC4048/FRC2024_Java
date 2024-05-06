package frc.robot.subsystems.swervev3.io;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class SwerveModuleInput implements LoggableInputs {

    public double[] steerEncoderPosition = new double[0];
    public double[] driveEncoderPosition = new double[0];
    public double[] driveEncoderVelocity = new double[0];
    public double[] steerEncoderVelocity = new double[0];
    public double[] measurementTimestamps = new double[0];
    public double driveCurrentDraw = 0;
    public double steerCurrentDraw = 0;
    public double steerOffset = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("steerEncoderPosition", steerEncoderPosition);
        table.put("driveEncoderPosition", driveEncoderPosition);
        table.put("driveEncoderVelocity", driveEncoderVelocity);
        table.put("steerEncoderVelocity", steerEncoderVelocity);
        table.put("measurementTimestamps", measurementTimestamps);
        table.put("driveCurrentDraw", driveCurrentDraw);
        table.put("steerCurrentDraw", steerCurrentDraw);
        table.put("steerOffset", steerOffset);
    }

    @Override
    public void fromLog(LogTable table) {
        steerEncoderPosition = table.get("steerEncoderValue", steerEncoderPosition);
        driveEncoderPosition = table.get("driveEncoderValue", driveEncoderPosition);
        driveEncoderVelocity = table.get("driveEncoderVelocity", driveEncoderVelocity);
        steerEncoderVelocity = table.get("steerEncoderVelocity", steerEncoderVelocity);
        measurementTimestamps = table.get("measurementTimestamps", measurementTimestamps);
        driveCurrentDraw = table.get("driveCurrentDraw", driveCurrentDraw);
        steerCurrentDraw = table.get("steerCurrentDraw", steerCurrentDraw);
        steerOffset = table.get("steerOffset", steerOffset);
    }
}
