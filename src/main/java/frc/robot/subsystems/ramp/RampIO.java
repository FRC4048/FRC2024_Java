package frc.robot.subsystems.ramp;

import frc.robot.subsystems.LoggableIO;

public interface RampIO extends LoggableIO<RampInputs> {
    void setP(double p);
    void setI(double i);
    void setD(double d);
    void setFF(double ff);
    void setRampPos(double targetPos);
    void setSpeed(double spd);
    void stopMotor();
    void resetEncoder();
}
