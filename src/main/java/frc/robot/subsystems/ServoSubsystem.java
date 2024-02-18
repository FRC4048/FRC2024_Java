// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class ServoSubsystem extends SubsystemBase {
    private final Servo servo;

    public ServoSubsystem() {
        this.servo = new Servo(Constants.SERVO_ID);
    }

    public void setServoAngle(double degrees) {
        servo.setAngle(degrees);
    }
}
