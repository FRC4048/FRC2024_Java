package frc.robot.subsystems.gyro;

public interface GyroIO {
    default void setAngleOffset(double offset) {}
    default void updateInputs(GyroInputs inputs){}
}
