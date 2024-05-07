package frc.robot.subsystems.gyro;

import frc.robot.subsystems.LoggableIO;

public interface GyroIO extends LoggableIO<GyroInputs> {
    void setAngleOffset(double offset);
    void resetGyro();
}
