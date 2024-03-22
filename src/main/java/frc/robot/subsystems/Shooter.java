package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.utils.NeoPidMotor;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Shooter extends SubsystemBase {
  private final String baseLogName = "/robot/shooter/";
  private final NeoPidMotor neoPidMotorLeft;
  private final NeoPidMotor neoPidMotorRight;
  private double leftCurrent;
  private double rightCurrent;
  private double totalCurrent;

  public Shooter() {
    neoPidMotorLeft = new NeoPidMotor(Constants.SHOOTER_MOTOR_LEFT);
    neoPidMotorRight = new NeoPidMotor(Constants.SHOOTER_MOTOR_RIGHT);

    neoPidMotorLeft.setPid(GameConstants.SHOOTER_PID_P, GameConstants.SHOOTER_PID_I, GameConstants.SHOOTER_PID_D, GameConstants.SHOOTER_PID_FF);
    neoPidMotorRight.setPid(GameConstants.SHOOTER_PID_P, GameConstants.SHOOTER_PID_I, GameConstants.SHOOTER_PID_D, GameConstants.SHOOTER_PID_FF);

    neoPidMotorLeft.setMaxAccel(Constants.SHOOTER_MAX_RPM_ACCELERATION);
    neoPidMotorRight.setMaxAccel(Constants.SHOOTER_MAX_RPM_ACCELERATION);
    
    neoPidMotorLeft.setMinMaxVelocity(30000, 0);
    neoPidMotorRight.setMinMaxVelocity(30000, 0);

    neoPidMotorLeft.setInverted(false);
    neoPidMotorRight.setInverted(true);

    neoPidMotorLeft.setIdleMode(IdleMode.kBrake);
    neoPidMotorRight.setIdleMode(IdleMode.kBrake);


  }

  /**
   * Set shooter motor 1 RPM with PID
   * @param rpm of motor
   */
  public void setShooterMotorLeftRPM(double rpm) {
    neoPidMotorLeft.setPidSpeed(rpm);
  }

  /**
   * Set shooter motor 2 RPM with PID
   * @param rpm of motor
   */
  public void setShooterMotorRightRPM(double rpm) {
    neoPidMotorRight.setPidSpeed(rpm);
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
    return neoPidMotorLeft.getCurrentSpeed();
  }

  /**
   * @return rpm of motor 2
   */
  public double getShooterMotorRightRPM() {
    return neoPidMotorRight.getCurrentSpeed();
  }


  /**
   * sets speed of motor1 and motor2 to 0
   */
  public void stopShooter() {
    neoPidMotorLeft.setPidSpeed(0);
    neoPidMotorRight.setPidSpeed(0);
  }

  @Override
  public void periodic() {
    leftCurrent = neoPidMotorLeft.getNeoMotor().getOutputCurrent();
    rightCurrent = neoPidMotorRight.getNeoMotor().getOutputCurrent();
    totalCurrent = leftCurrent + rightCurrent;
    if (Constants.SHOOTER_DEBUG){
      SmartShuffleboard.put("Shooter", "Left Shooter Motor RPM", getShooterMotorLeftRPM());
      SmartShuffleboard.put("Shooter", "Right Shooter Motor RPM", getShooterMotorRightRPM());
      SmartShuffleboard.put("Shooter", "Left Motor Current", leftCurrent);
      SmartShuffleboard.put("Shooter", "Right Motor Current", rightCurrent);
      SmartShuffleboard.put("Shooter", "Total Motor Current", totalCurrent);
      SmartShuffleboard.put("Shooter", "Reverse Switch Tripped", neoPidMotorLeft.forwardLimitSwitchIsPressed());
            SmartShuffleboard.put("Shooter", "Forward Switch Tripped", neoPidMotorLeft.reversedLimitSwitchIsPressed());
    }
    Logger.logDouble(baseLogName + "motorLeftRPM", getShooterMotorLeftRPM(), Constants.ENABLE_LOGGING);
    Logger.logDouble(baseLogName + "motorRightRPM", getShooterMotorRightRPM(), Constants.ENABLE_LOGGING);
  }

  public boolean upToSpeed(double leftSpeed, double rightSpeed) {
    return ((getShooterMotorLeftRPM() / leftSpeed) * 100 > Constants.SHOOTER_UP_TO_SPEED_THRESHOLD) &&
            ((getShooterMotorRightRPM() / rightSpeed) * 100 > Constants.SHOOTER_UP_TO_SPEED_THRESHOLD);
  }
}
