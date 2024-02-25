package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Shooter extends SubsystemBase {
  
  private final CANSparkMax shooterMotorLeft;
  private final CANSparkMax shooterMotorRight;
  private final DigitalInput shooterSensorLeft;
  private final DigitalInput shooterSensorRight;
  private final SparkPIDController shooterMotor1PID;
  private final SparkPIDController shooterMotor2PID;

  public Shooter() {
    this.shooterMotorLeft = new CANSparkMax(Constants.SHOOTER_MOTOR_LEFT, CANSparkLowLevel.MotorType.kBrushless);
    this.shooterMotorRight = new CANSparkMax(Constants.SHOOTER_MOTOR_RIGHT, CANSparkLowLevel.MotorType.kBrushless);
    this.shooterSensorLeft = new DigitalInput(Constants.SHOOTER_SENSOR_ID_1);
    this.shooterSensorRight = new DigitalInput(Constants.SHOOTER_SENSOR_ID_2);

    shooterMotorLeft.restoreFactoryDefaults();
    shooterMotorRight.restoreFactoryDefaults();

    shooterMotorLeft.setInverted(false);
    shooterMotorRight.setInverted(true);

    this.shooterMotorLeft.setIdleMode(IdleMode.kCoast);
    this.shooterMotorRight.setIdleMode(IdleMode.kCoast);
  
    this.shooterMotor1PID = shooterMotorLeft.getPIDController();
    this.shooterMotor2PID = shooterMotorRight.getPIDController();

    shooterMotor1PID.setP(Constants.SHOOTER_MOTOR_PID_P);
    shooterMotor1PID.setI(Constants.SHOOTER_MOTOR_PID_I);
    shooterMotor1PID.setD(Constants.SHOOTER_MOTOR_PID_D);
    shooterMotor1PID.setIZone(Constants.SHOOTER_MOTOR_PID_IZ);
    shooterMotor1PID.setFF(Constants.SHOOTER_MOTOR_PID_FF);
    shooterMotor1PID.setOutputRange(Constants.SHOOTER_MOTOR_MIN_OUTPUT, Constants.SHOOTER_MOTOR_MAX_OUTPUT);

    shooterMotor2PID.setP(Constants.SHOOTER_MOTOR_PID_P);
    shooterMotor2PID.setI(Constants.SHOOTER_MOTOR_PID_I);
    shooterMotor2PID.setD(Constants.SHOOTER_MOTOR_PID_D);
    shooterMotor2PID.setIZone(Constants.SHOOTER_MOTOR_PID_IZ);
    shooterMotor2PID.setFF(Constants.SHOOTER_MOTOR_PID_FF);
    shooterMotor2PID.setOutputRange(Constants.SHOOTER_MOTOR_MIN_OUTPUT, Constants.SHOOTER_MOTOR_MAX_OUTPUT);
  }
  /**
   * @param speed value between -1 and 1 to set shooter motor 1 to
   */
  public void setShooterMotorLeftSpeed(double speed) {
    shooterMotorLeft.set(speed);
  }

  /**
   * @param speed value between -1 and 1 to set shooter motor 2 to
   */
  public void setShooterMotorRightSpeed(double speed) {
    shooterMotorRight.set(speed);
  }

  /**
   * @return shooter motor 1 speed
   */
  public double getShooterMotor1Speed() {
    return shooterMotorLeft.get();
  }

  /**
   * @return shooter motor 2 speed
   */
  public double getShooterMotor2Speed() {
    return shooterMotorRight.get();
  }

  /**
   * Set shooter motor 1 RPM with PID
   * @param rpm of motor
   */
  public void setShooterMotor1RPM(double rpm) {
    shooterMotor1PID.setReference(rpm, CANSparkMax.ControlType.kVelocity);
  }

  /**
   * Set shooter motor 2 RPM with PID
   * @param rpm of motor
   */
  public void setShooterMotor2RPM(double rpm) {
    shooterMotor2PID.setReference(rpm, CANSparkMax.ControlType.kVelocity);
  }

  /**
   * @return motor 1 encoder value
   */
  public RelativeEncoder getMotorEncoder1() {
    return shooterMotorLeft.getEncoder();
  }

  /**
   * @return motor 2 encoder value
   */
  public RelativeEncoder getMotorEncoder2() {
    return shooterMotorRight.getEncoder();
  }

  /**
   * @return rpm of motor 1
   */
  public double getShooterMotor1RPM() {
    return getMotorEncoder1().getVelocity();
  }

  /**
   * @return rpm of motor 2
   */
  public double getShooterMotor2RPM() {
    return getMotorEncoder2().getVelocity();
  }


  /**
   * sets speed of motor1 and motor2 to 0
   */
  public void stopShooter() {
    shooterMotorLeft.set(0);
    shooterMotorRight.set(0);
  }

  /**
   * @return returns true if shooter motor 1 is connected to digital IO
   */
  public boolean getShooterSensor1Activated() {
    return shooterSensorLeft.get();
  }

  /**
   * @return returns true if shooter motor 2 is connected to digital IO
   */
  public boolean getShooterSensor2Activated() {
    return shooterSensorRight.get();
  }

  @Override
  public void periodic() {
    if (Constants.SHOOTER_DEBUG){
      SmartShuffleboard.put("Shooter", "Shooter Motor 1 Speed", getShooterMotor1Speed());
      SmartShuffleboard.put("Shooter", "Shooter Motor 2 Speed", getShooterMotor2Speed());
      SmartShuffleboard.put("Shooter", "Shooter Motor 1 RPM", getShooterMotor1RPM());
      SmartShuffleboard.put("Shooter", "Shooter Motor 2 RPM", getShooterMotor2RPM());
      SmartShuffleboard.put("Shooter", "Shooter Sensor 1", getShooterSensor1Activated());
      SmartShuffleboard.put("Shooter", "Shooter Sensor 2", getShooterSensor2Activated());
      SmartShuffleboard.put("Diagnostics","Shooter", "Shooter Sensor 1", getShooterSensor1Activated());
      SmartShuffleboard.put("Diagnostics","Shooter", "Shooter Sensor 2", getShooterSensor2Activated());
      
    }
  }
}
