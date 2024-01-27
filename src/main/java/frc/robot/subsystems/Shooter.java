package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DigitalInput;

public class Shooter extends SubsystemBase {
  
  private final CANSparkMax shooterMotor1;
  private final CANSparkMax shooterMotor2;
  private final DigitalInput shooterSensor1;
  private final DigitalInput shooterSensor2;
  private final SparkPIDController shooterMotor1PID;
  private final SparkPIDController shooterMotor2PID;

  public Shooter() {
    
    this.shooterMotor1 = new CANSparkMax(Constants.SHOOTER_MOTOR_ID_1, CANSparkLowLevel.MotorType.kBrushless);
    this.shooterMotor2 = new CANSparkMax(Constants.SHOOTER_MOTOR_ID_2, CANSparkLowLevel.MotorType.kBrushless);
    this.shooterSensor1 = new DigitalInput(Constants.SHOOTER_SENSOR_ID_1);
    this.shooterSensor2 = new DigitalInput(Constants.SHOOTER_SENSOR_ID_2);

    shooterMotor1.restoreFactoryDefaults();
    shooterMotor2.restoreFactoryDefaults();

    this.shooterMotor1.setIdleMode(IdleMode.kCoast);
    this.shooterMotor2.setIdleMode(IdleMode.kCoast);
  
    this.shooterMotor1PID = shooterMotor1.getPIDController();
    this.shooterMotor2PID = shooterMotor2.getPIDController();

    //Set up PID for motor 1
    shooterMotor1PID.setP(Constants.SHOOTER_MOTOR_PID_P);
    shooterMotor1PID.setI(Constants.SHOOTER_MOTOR_PID_I);
    shooterMotor1PID.setD(Constants.SHOOTER_MOTOR_PID_D);
    shooterMotor1PID.setIZone(Constants.SHOOTER_MOTOR_PID_IZ);
    shooterMotor1PID.setFF(Constants.SHOOTER_MOTOR_PID_FF);
    shooterMotor1PID.setOutputRange(Constants.SHOOTER_MOTOR_MIN_OUTPUT, Constants.SHOOTER_MOTOR_MAX_OUTPUT);

    //Set up PID for motor 2
    shooterMotor2PID.setP(Constants.SHOOTER_MOTOR_PID_P);
    shooterMotor2PID.setI(Constants.SHOOTER_MOTOR_PID_I);
    shooterMotor2PID.setD(Constants.SHOOTER_MOTOR_PID_D);
    shooterMotor2PID.setIZone(Constants.SHOOTER_MOTOR_PID_IZ);
    shooterMotor2PID.setFF(Constants.SHOOTER_MOTOR_PID_FF);
    shooterMotor2PID.setOutputRange(Constants.SHOOTER_MOTOR_MIN_OUTPUT, Constants.SHOOTER_MOTOR_MAX_OUTPUT);
  }

  //Spin shooter motor 1
  public void setShooterMotor1Speed(double speed) {
    shooterMotor1.set(speed);
  }

  //Spin shooter motor 2
  public void setShooterMotor2Speed(double speed) {
    shooterMotor2.set(speed);
  }

  //Get shooter motor 1 speed
  public double getShooterMotor1Speed() {
    return shooterMotor1.get();
  }

  //Get shooter motor 2 speed
  public double getShooterMotor2Speed() {
    return shooterMotor2.get();
  }

  //Set shooter motor 1 RPM with PID (preffered)
  public void setShooterMotor1RPM(double rpm) {
    shooterMotor1PID.setReference(rpm, CANSparkMax.ControlType.kVelocity);
  }

  //Set shooter motor 2 RPM with PID (preffered)
  public void setShooterMotor2RPM(double rpm) {
    shooterMotor2PID.setReference(rpm, CANSparkMax.ControlType.kVelocity);
  }

  //Get motor 1 encodor
  public RelativeEncoder getMotorEncodor1() {
    return shooterMotor1.getEncoder();
  }

  //Get motor 2 encodor
  public RelativeEncoder getMotorEncodor2() {
    return shooterMotor2.getEncoder();
  }

  //Get motor 1 speed RPM
  public double getShooterMotor1RPM() {
    return getMotorEncodor1().getVelocity();
  }

  //Get motor 2 speed RPM
  public double getShooterMotor2RPM() {
    return getMotorEncodor2().getVelocity();
  }

  //Stop shooter motors
  public void stopShooter() {
    shooterMotor1.set(0);
    shooterMotor2.set(0);
  }

  //Get the status of the shooter sensor 1
  public boolean getShooterSensor1Activated() {
    return shooterSensor1.get();
  }

  //Get the status of the shooter sensor 2
  public boolean getShooterSensor2Activated() {
    return shooterSensor2.get();
  }

  @Override
  public void periodic() {
    //Update the Shuffleboard
    SmartShuffleboard.put("Shooter", "Shooter Motor 1 Speed", getShooterMotor1Speed());
    SmartShuffleboard.put("Shooter", "Shooter Motor 2 Speed", getShooterMotor2Speed());
    SmartShuffleboard.put("Shooter", "Shooter Motor 1 RPM", getShooterMotor1RPM());
    SmartShuffleboard.put("Shooter", "Shooter Motor 2 RPM", getShooterMotor2RPM());
    SmartShuffleboard.put("Shooter", "Shooter Sensor 1", getShooterSensor1Activated());
    SmartShuffleboard.put("Shooter", "Shooter Sensor 2", getShooterSensor2Activated());
  }

}
