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
  private double servoPosition;
  private double servoSpeed;
  /** Creates a new Servo. */
  public ServoSubsystem() {
    this.servo = new Servo(Constants.SERVO_ID);
  }
  public void set_Servo(double position) {
    servo.set(position);
  }
  public double get_Servo() {
    return servoPosition;
  }
  public double get_Speed() {
    return servoSpeed;
  }
  @Override
  public void periodic() {
    servoPosition=servo.getPosition();
    servoSpeed=servo.getSpeed();
    if (Constants.CLIMBER_DEBUG) {
      SmartShuffleboard.put("Climber", "Climber Servo Position", servoPosition);
      SmartShuffleboard.put("Climber", "Climber Servo Speed", servoSpeed);
    }
  }
}
