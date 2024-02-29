package frc.robot.utils;

public class ShooterSpeed {
    private final double leftMotorSpeed;
    private final double rightMotorSpeed;

    public ShooterSpeed(double leftMotorSpeed, double rightMotorSpeed) {
        this.leftMotorSpeed = leftMotorSpeed;
        this.rightMotorSpeed = rightMotorSpeed;
    }

    public double getLeftMotorSpeed() {
        return leftMotorSpeed;
    }

    public double getRightMotorSpeed() {
        return rightMotorSpeed;
    }
}
