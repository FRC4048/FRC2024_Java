package frc.robot.subsystems.intake;

public interface IntakeIO {
    void setMotorSpeeds(double m1Speed, double m2Speed);
    void stopMotors();
    void updateInputs(IntakeInputs inputs);
}
