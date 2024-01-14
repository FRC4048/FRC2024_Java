package frc.robot.subsystems;

import frc.robot.utils.SmartShuffleboard;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.DigitalInput;

public class Shooter extends SubsystemBase {
  
  private final PWMSparkMax shooterWheel1;
  private final PWMSparkMax shooterWheel2;
  private final DigitalInput shooterSensor;

  public Shooter() {
    
    this.shooterWheel1 = new PWMSparkMax(Constants.SHOOTER_MOTOR_ID_1);
    this.shooterWheel2 = new PWMSparkMax(Constants.SHOOTER_MOTOR_ID_2);
    this.shooterSensor = new DigitalInput(Constants.SHOOTER_SENSOR_ID);

    SmartShuffleboard.put("Shooter Motors", "Shooter Motor 1", shooterWheel1.get());
    SmartShuffleboard.put("Shooter Motors", "Shooter Motor 2", shooterWheel2.get());
    SmartShuffleboard.put("Shooter Sensor", "Shooter Sensor 1", shooterSensor.get());
  
  }

  //Spin shooter motors
  public void spinMotors(double speed) {
    shooterWheel1.set(speed);
    shooterWheel2.set(speed);
  }

  //Stop shooter motors
  public void stopMotor() {
    shooterWheel1.set(0);
    shooterWheel2.set(0);
  }

  //Get the status of the shooter sensor
  public boolean getShooterSensorActivated() {
    return shooterSensor.get();
  }

}
