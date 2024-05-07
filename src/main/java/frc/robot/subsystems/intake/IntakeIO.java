package frc.robot.subsystems.intake;

import frc.robot.subsystems.LoggableIO;

public interface IntakeIO extends LoggableIO<IntakeInputs> {
    void setMotorSpeeds(double m1Speed, double m2Speed);
    void stopMotors();
}
