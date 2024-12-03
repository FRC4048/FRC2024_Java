package frc.robot.subsystems.swervev3.io.drive;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class SwerveDriveMotorInput implements LoggableInputs {

    public double driveEncoderPosition = 0;
    public double driveEncoderVelocity = 0;
    public double driveCurrentDraw = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("driveEncoderPosition", driveEncoderPosition);
        table.put("driveEncoderVelocity", driveEncoderVelocity);
        table.put("driveCurrentDraw", driveCurrentDraw);
    }

    @Override
    public void fromLog(LogTable table) {
        driveEncoderPosition = table.get("driveEncoderPosition", driveEncoderPosition);
        driveEncoderVelocity = table.get("driveEncoderVelocity", driveEncoderVelocity);
        driveCurrentDraw = table.get("driveCurrentDraw", driveCurrentDraw);
    }
}
