package frc.robot.subsystems.climber;

import frc.robot.subsystems.LoggableIO;

public interface ClimberIO extends LoggableIO<ClimberInputs> {
    void setLeftSpeed(double spd);
    void setRightSpeed(double spd);
    void resetEncoder();
    void setRatchetPos(double servo1Pos, double servo2Pos);
}
