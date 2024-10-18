package frc.robot.subsystems.apriltags;

public record ApriltagReading(double posX, double posY, double rotationDeg, double latency, double measurementTime) {
}
