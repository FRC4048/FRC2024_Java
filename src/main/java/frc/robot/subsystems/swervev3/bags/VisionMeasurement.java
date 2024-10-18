package frc.robot.subsystems.swervev3.bags;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.utils.advanced.Apriltag;

public record VisionMeasurement(Pose2d measurement, Apriltag tag, double timeOfMeasurement)  {
}
