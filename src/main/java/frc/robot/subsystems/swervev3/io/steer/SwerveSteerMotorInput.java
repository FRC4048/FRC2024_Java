package frc.robot.subsystems.swervev3.io.steer;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class SwerveSteerMotorInput implements LoggableInputs {
    public double steerEncoderPosition = 0;
    public double steerEncoderVelocity = 0;
    public double steerCurrentDraw = 0;

    @Override
    public void toLog(LogTable table) {
        table.put("steerEncoderPosition", steerEncoderPosition);
        table.put("steerEncoderVelocity", steerEncoderVelocity);
        table.put("steerCurrentDraw", steerCurrentDraw);
    }

    @Override
    public void fromLog(LogTable table) {
        steerEncoderPosition = table.get("steerEncoderPosition", steerEncoderPosition);
        steerEncoderVelocity = table.get("steerEncoderVelocity", steerEncoderVelocity);
        steerCurrentDraw = table.get("steerCurrentDraw", steerCurrentDraw);
    }
}
