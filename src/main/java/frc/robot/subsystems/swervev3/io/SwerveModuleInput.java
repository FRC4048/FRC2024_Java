package frc.robot.subsystems.swervev3.io;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class SwerveModuleInput implements LoggableInputs {

    public double steerEncoderPosition = 0;
    public double driveEncoderPosition = 0;
    public double driveEncoderVelocity = 0;
    public double steerEncoderVelocity = 0;
    public double driveCurrentDraw = 0;
    public double steerCurrentDraw = 0;
    public double steerOffset = 0;

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
        steerEncoderPosition = table.get("steerEncoderPosition", steerEncoderPosition);
        driveEncoderPosition = table.get("driveEncoderPosition", driveEncoderPosition);
        driveEncoderVelocity = table.get("driveEncoderVelocity", driveEncoderVelocity);
        steerEncoderVelocity = table.get("steerEncoderVelocity", steerEncoderVelocity);
        driveCurrentDraw = table.get("driveCurrentDraw", driveCurrentDraw);
        steerCurrentDraw = table.get("steerCurrentDraw", steerCurrentDraw);
        steerOffset = table.get("steerOffset", steerOffset);
    }
}
