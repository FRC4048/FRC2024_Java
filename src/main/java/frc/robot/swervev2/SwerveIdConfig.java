package frc.robot.swervev2;

public class SwerveIdConfig {
    private final int driveMotorId;
    private final int turnMotorId;
    private final int analogEncoderId;

    public SwerveIdConfig(int driveMotorId, int turnMotorId, int analogEncoderId) {
        this.driveMotorId = driveMotorId;
        this.turnMotorId = turnMotorId;
        this.analogEncoderId = analogEncoderId;
    }

    public int getDriveMotorId() {
        return driveMotorId;
    }

    public int getTurnMotorId() {
        return turnMotorId;
    }

    public int getanalogEncoderId() {
        return analogEncoderId;
    }
}