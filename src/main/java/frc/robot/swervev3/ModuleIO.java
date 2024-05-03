package frc.robot.swervev3;

public interface ModuleIO {
    void setDriveVoltage(double volts);
    void setSteerVoltage(double volts);
    void setSteerOffset(double offset);
    void resetEncoder();
    void updateInputs(SwerveModuleInput input);
}
