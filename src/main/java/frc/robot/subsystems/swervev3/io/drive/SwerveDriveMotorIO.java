package frc.robot.subsystems.swervev3.io.drive;


import frc.robot.subsystems.LoggableIO;

public interface SwerveDriveMotorIO extends LoggableIO<SwerveDriveMotorInput> {
    void setDriveVoltage(double volts);
    void resetEncoder();
}
