package frc.robot.subsystems.swervev3.vision;

import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.swervev3.bags.VisionMeasurement;

public interface VisionTransformer {
    Pose2d getVisionPose(VisionMeasurement measurement);
}
