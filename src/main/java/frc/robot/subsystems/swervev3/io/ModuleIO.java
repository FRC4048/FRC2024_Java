package frc.robot.subsystems.swervev3.io;

import frc.robot.subsystems.LoggableIO;

public interface ModuleIO extends LoggableIO<SwerveModuleInput> {
    void setDriveVoltage(double volts);
    void setSteerVoltage(double volts);
    void setSteerOffset(double offset);
    void resetEncoder();
}
