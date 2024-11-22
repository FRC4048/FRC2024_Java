package frc.robot.swervev2.components;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;

public interface SwerveMotor {
    MotorController getSteerMotor();
    MotorController getDriveMotor();
}