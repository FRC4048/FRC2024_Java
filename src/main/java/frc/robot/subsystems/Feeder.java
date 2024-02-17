package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Feeder extends SubsystemBase{

    private final WPI_TalonSRX feederMotor;
    private final DigitalInput feederSensor;

    public Feeder() {
        this.feederMotor = new WPI_TalonSRX(Constants.FEEDER_MOTOR_ID);
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

    /**
     * @return returns true if feeder sensor is connected to digital IO
     */
    public boolean getFeederSensor() {
        return feederSensor.get();
    }

    @Override
    public void periodic() {
        if (Constants.FEEDER_DEBUG){
            SmartShuffleboard.put("Feeder", "Feeder Motor Speed", getFeederMotorSpeed());
            SmartShuffleboard.put("Feeder", "Feeder Sensor", getFeederSensor());
        }
    }
}