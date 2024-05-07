package frc.robot.subsystems.feeder;

import frc.robot.subsystems.LoggableIO;

public interface FeederIO extends LoggableIO<FeederInputs> {
    void setSpeed(double spd);
    void stop();
    void switchFeederBeamState(boolean enable);
}
