package frc.robot.subsystems.swervev3.io.steer;


import frc.robot.subsystems.LoggableIO;

public interface SwerveSteerMotorIO extends LoggableIO<SwerveSteerMotorInput> {
    void setSteerVoltage(double volts);
    void resetEncoder();
}
