package frc.robot.subsystems.climber;

public interface ClimberIO {
    void setLeftSpeed(double spd);
    void setRightSpeed(double spd);
    void resetEncoder();
    void setRatchetPos(double servo1Pos, double servo2Pos);
    void updateInputs(ClimberInputs inputs);
}
