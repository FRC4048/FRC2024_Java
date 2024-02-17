// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class ServoSubsystem extends SubsystemBase {
  private final Servo servo;
  /** Creates a new Servo. */
  public ServoSubsystem() {
    this.servo = new Servo(Constants.SERVO_ID);
  }
  public void setServoAngle(double degrees) {
    servo.setAngle(degrees);
  }
  public double getServo() {
    return servo.getPosition();
  }
  public double getSpeed() {
    return servo.getSpeed();
  }
  @Override
  public void periodic() {
    if (Constants.CLIMBER_DEBUG) {
      SmartShuffleboard.put("Climber", "Climber Servo Position", getServo());
      SmartShuffleboard.put("Climber", "Climber Servo Speed", getSpeed());
    }
  }
}
