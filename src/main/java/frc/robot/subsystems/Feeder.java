package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;
import edu.wpi.first.wpilibj.motorcontrol.PWMTalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;

public class Feeder extends SubsystemBase{

    private final PWMTalonSRX feederMotor;
    private final DigitalInput feederSensor;

    public Feeder() {
        this.feederMotor = new PWMTalonSRX(Constants.FEEDER_MOTOR_ID);
        this.feederSensor = new DigitalInput(Constants.FEEDER_SENSOR_ID);
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

    public boolean getFeederSensor() {
        return feederSensor.get();
    }

    @Override
    public void periodic() {
        SmartShuffleboard.put("Feeder", "Feeder Motor Speed", getFeederMotorSpeed());
    }
}