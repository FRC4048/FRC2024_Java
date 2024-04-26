package frc.robot.subsystems.feeder;

public interface FeederIO {
    void setSpeed(double spd);
    void stop();
    void switchFeederBeamState(boolean enable);
    void updateInputs(FeederInputs inputs);
}
