package frc.robot.subsystems.climber;

public interface ClimberIO {
    void setLeftSpeed(double spd);
    void setRightSpeed(double spd);
    void resetEncoder();
    void engageRatchet();
    void disengageRatchet();
    void updateInputs(ClimberInputs inputs);
}
