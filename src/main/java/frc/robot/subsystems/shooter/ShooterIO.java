package frc.robot.subsystems.shooter;

public interface ShooterIO {
    void slowStop();
    void stop();
    void setShooterLeftRPM(double rpm);
    void setShooterRightRPM(double rpm);
    void updateInputs(ShooterInput input);
}
