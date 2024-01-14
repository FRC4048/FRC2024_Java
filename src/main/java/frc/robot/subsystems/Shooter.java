package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;
import edu.wpi.first.wpilibj.DigitalInput;

public class Shooter extends SubsystemBase {
  
  private final CANSparkMax shooterWheel1;
  private final CANSparkMax shooterWheel2;
  private final DigitalInput shooterSensor;

  public Shooter() {
    
    this.shooterWheel1 = new CANSparkMax(Constants.SHOOTER_MOTOR_ID_1, CANSparkLowLevel.MotorType.kBrushless);
    this.shooterWheel2 = new CANSparkMax(Constants.SHOOTER_MOTOR_ID_2, CANSparkLowLevel.MotorType.kBrushless);
    this.shooterSensor = new DigitalInput(Constants.SHOOTER_SENSOR_ID);

    shooterWheel1.restoreFactoryDefaults();
    shooterWheel2.restoreFactoryDefaults();

    this.shooterWheel1.setIdleMode(IdleMode.kCoast);
    this.shooterWheel2.setIdleMode(IdleMode.kCoast);

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
