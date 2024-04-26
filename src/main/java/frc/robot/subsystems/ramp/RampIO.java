package frc.robot.subsystems.ramp;

public interface RampIO {
    void setP(double p);
    void setI(double i);
    void setD(double d);
    void setFF(double ff);
    void setRampPos(double targetPos);
    void setSpeed(double spd);
    void stopMotor();
    void resetEncoder();
    void updateInputs(RampInputs inputs);
}
