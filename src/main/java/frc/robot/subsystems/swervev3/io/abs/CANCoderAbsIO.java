package frc.robot.subsystems.swervev3.io.abs;

import com.ctre.phoenix.sensors.WPI_CANCoder;

public class CANCoderAbsIO implements SwerveAbsIO {
    private final WPI_CANCoder absEncoder;

    public CANCoderAbsIO(int canCoderID) {
        absEncoder = new WPI_CANCoder(canCoderID);
    }

    @Override
    public void updateInputs(SwerveAbsInput input) {
        input.absEncoderPosition = absEncoder.getAbsolutePosition();
    }
}
