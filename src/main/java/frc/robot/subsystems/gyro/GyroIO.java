package frc.robot.subsystems.gyro;

public interface GyroIO {
    void setAngleOffset(double offset);
    void resetGyro();
    void updateInputs(GyroInputs inputs);
}
