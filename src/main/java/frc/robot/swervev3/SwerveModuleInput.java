package frc.robot.swervev3;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class SwerveModuleInput implements LoggableInputs {

    public double steerEncoderPosition;
    public double driveEncoderPosition;
    public double driveEncoderVelocity;
    public double steerEncoderVelocity;
    public double driveCurrentDraw;
    public double steerCurrentDraw;
    public double steerOffset;

    @Override
    public void toLog(LogTable table) {
        table.put("steerEncoderPosition", steerEncoderPosition);
        table.put("driveEncoderPosition", driveEncoderPosition);
        table.put("driveEncoderVelocity", driveEncoderVelocity);
        table.put("steerEncoderVelocity", steerEncoderVelocity);
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
        driveCurrentDraw = table.get("driveCurrentDraw", driveCurrentDraw);
        steerCurrentDraw = table.get("steerCurrentDraw", steerCurrentDraw);
        steerOffset = table.get("steerOffset", steerOffset);
    }
}
