package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase{

    private final PWMTalonSRX feederMotor;

    public Feeder() {
        this.feederMotor = new PWMTalonSRX(Constants.FEEDER_MOTOR_ID);
    }

    public void setFeederMotorSpeed(double speed) {
        feederMotor.set(speed);
    }

    public double getFeederMotorSpeed() {
        return feederMotor.get();
    }

    public void stopFeederMotor() {
        feederMotor.set(0);
    }

    @Override
    public void periodic() {
        SmartShuffleboard.put("Feeder", "Feeder Motor Speed", getFeederMotorSpeed());
    }
}