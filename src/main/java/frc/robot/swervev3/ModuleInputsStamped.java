package frc.robot.swervev3;

public record ModuleInputsStamped(double steerEncoderPosition, double driveEncoderPosition,
                                  double driveEncoderVelocity, double steerEncoderVelocity,
                                  double measurementTimestamp) {
}
