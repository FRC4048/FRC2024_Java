package frc.robot.subsystems.swervev3.bags;

public record ModuleInputsStamped(double steerEncoderPosition, double driveEncoderPosition,
                                  double driveEncoderVelocity, double steerEncoderVelocity,
                                  double measurementTimestamp) {
}
