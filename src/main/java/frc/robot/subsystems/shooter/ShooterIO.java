package frc.robot.subsystems.shooter;

import frc.robot.subsystems.LoggableIO;

public interface ShooterIO extends LoggableIO<ShooterInputs> {
    void slowStop();
    void stop();
    void setShooterLeftRPM(double rpm);
    void setShooterRightRPM(double rpm);
}
