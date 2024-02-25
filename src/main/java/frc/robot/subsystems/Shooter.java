package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.NeoPidMotor;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Shooter extends SubsystemBase {
  
  private final CANSparkMax shooterMotorLeft;
  private final CANSparkMax shooterMotorRight;
  private final DigitalInput shooterSensorLeft;
  private final DigitalInput shooterSensorRight;
  private final NeoPidMotor neoPidMotorLeft;
  private final NeoPidMotor neoPidMotorRight;

  public Shooter() {
    neoPidMotorLeft = new NeoPidMotor(Constants.SHOOTER_MOTOR_LEFT);
    neoPidMotorRight = new NeoPidMotor(Constants.SHOOTER_MOTOR_RIGHT);
    neoPidMotorLeft.setSmartMotionMaxAccel(22800, 0);
    neoPidMotorRight.setSmartMotionMaxAccel(22800, 0);
    neoPidMotorLeft.setSmartMotionVelocity(22800, 0, 0);
    neoPidMotorRight.setSmartMotionVelocity(22800, 0, 0);
    this.shooterMotorLeft = neoPidMotorLeft.getNeoMotor();
    this.shooterMotorRight = neoPidMotorRight.getNeoMotor();
    this.shooterSensorLeft = new DigitalInput(Constants.SHOOTER_SENSOR_ID_1);
    this.shooterSensorRight = new DigitalInput(Constants.SHOOTER_SENSOR_ID_2);

    shooterMotorLeft.setInverted(false);
    shooterMotorRight.setInverted(true);

    this.shooterMotorLeft.setIdleMode(IdleMode.kCoast);
    this.shooterMotorRight.setIdleMode(IdleMode.kCoast);

    neoPidMotorLeft.setPid(Constants.SHOOTER_MOTOR_PID_P, Constants.SHOOTER_MOTOR_PID_I, Constants.SHOOTER_MOTOR_PID_D, Constants.SHOOTER_MOTOR_PID_IZ, Constants.SHOOTER_MOTOR_PID_FF, Constants.SHOOTER_MOTOR_MIN_OUTPUT, Constants.SHOOTER_MOTOR_MAX_OUTPUT);
    neoPidMotorRight.setPid(Constants.SHOOTER_MOTOR_PID_P, Constants.SHOOTER_MOTOR_PID_I, Constants.SHOOTER_MOTOR_PID_D, Constants.SHOOTER_MOTOR_PID_IZ, Constants.SHOOTER_MOTOR_PID_FF, Constants.SHOOTER_MOTOR_MIN_OUTPUT, Constants.SHOOTER_MOTOR_MAX_OUTPUT);
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
  public double getShooterMotorLeftSpeed() {
    return shooterMotorLeft.get();
  }

  /**
   * @return shooter motor 2 speed
   */
  public double getShooterMotorRightSpeed() {
    return shooterMotorRight.get();
  }

  /**
   * Set shooter motor 1 RPM with PID
   * @param rpm of motor
   */
  public void setShooterMotorLeftRPM(double rpm) {
    neoPidMotorLeft.setPidSpeed(rpm * 4);
  }

  /**
   * Set shooter motor 2 RPM with PID
   * @param rpm of motor
   */
  public void setShooterMotorRightRPM(double rpm) {
    neoPidMotorRight.setPidSpeed(rpm * 4);
  }

  /**
   * @return motor 1 encoder value
   */
  public RelativeEncoder getMotorLeftEncoder() {
    return neoPidMotorLeft.getEncoder();
  }

  /**
   * @return motor 2 encoder value
   */
  public RelativeEncoder getMotorRightEncoder() {
    return neoPidMotorRight.getEncoder();
  }

  /**
   * @return rpm of motor 1
   */
  public double getShooterMotorLeftRPM() {
    return getMotorLeftEncoder().getVelocity();
  }

  /**
   * @return rpm of motor 2
   */
  public double getShooterMotorRightRPM() {
    return getMotorRightEncoder().getVelocity();
  }


  /**
   * sets speed of motor1 and motor2 to 0
   */
  public void stopShooter() {
    neoPidMotorLeft.setPidSpeed(0);
    neoPidMotorRight.setPidSpeed(0);
  }

  /**
   * @return returns true if shooter motor 1 is connected to digital IO
   */
  public boolean getShooterSensorLeftActivated() {
    return shooterSensorLeft.get();
  }

  /**
   * @return returns true if shooter motor 2 is connected to digital IO
   */
  public boolean getShooterSensorRightActivated() {
    return shooterSensorRight.get();
  }

  @Override
  public void periodic() {
    if (Constants.SHOOTER_DEBUG){
      SmartShuffleboard.put("Shooter", "Left Shooter Motor Speed", getShooterMotorLeftSpeed());
      SmartShuffleboard.put("Shooter", "Right Shooter Motor Speed", getShooterMotorRightSpeed());
      SmartShuffleboard.put("Shooter", "Left Shooter Motor RPM", getShooterMotorLeftRPM());
      SmartShuffleboard.put("Shooter", "Right Shooter Motor RPM", getShooterMotorRightRPM());
      SmartShuffleboard.put("Shooter", "Left Shooter Sensor 1", getShooterSensorLeftActivated());
      SmartShuffleboard.put("Shooter", "Right Shooter Sensor 2", getShooterSensorRightActivated());
    }
  }
}
