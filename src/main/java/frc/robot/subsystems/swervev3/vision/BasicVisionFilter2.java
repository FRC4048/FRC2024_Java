package frc.robot.subsystems.swervev3.vision;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.swervev3.bags.VisionMeasurement;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Queue;

public abstract class BasicVisionFilter2 implements VisionFilter, VisionTransformer {

    private final TimeInterpolatableBuffer<Pose2d> poseBuffer;

    public BasicVisionFilter2(TimeInterpolatableBuffer<Pose2d> poseBuffer) {
        this.poseBuffer = poseBuffer;
    }

    @Override
    public LinkedHashMap<VisionMeasurement, FilterResult> filter(Queue<VisionMeasurement> measurements) {
        LinkedHashMap<VisionMeasurement, FilterResult> resultMap = new LinkedHashMap<>();
        VisionMeasurement m1 = measurements.poll();
        VisionMeasurement m2 = measurements.peek();
        boolean processing = true;
        do {
            /*
            -------------------------------------------------------
            Handle Null Case
            -------------------------------------------------------
            */
            if (m1 == null){
                processing = false;
                if (m2 != null){
                    resultMap.put(m2, FilterResult.NOT_PROCESSED);
                }
                continue;
            } else if (m2 == null){
                resultMap.put(m1, FilterResult.NOT_PROCESSED);
                processing = false;
                continue;
            }
            /*
            -------------------------------------------------------
            Filter poses
            -------------------------------------------------------
            */
            Pose2d vision1Pose = getVisionPose(m1);
            Pose2d vision2Pose = getVisionPose(m2);
            boolean valid1 = filterVision(vision1Pose, vision2Pose, m1.timeOfMeasurement(), m2.timeOfMeasurement());
            resultMap.put(m1, valid1 ? FilterResult.ACCEPTED : FilterResult.REJECTED);
            m1 = measurements.poll();
            m2 = measurements.peek();

        } while(processing);
        return resultMap;
    }

    private boolean filterVision(Pose2d m1Pose, Pose2d m2Pose, double m1Time, double m2Time) {
        Optional<Pose2d> odomPoseAtVis1 = poseBuffer.getSample(m1Time);
        Optional<Pose2d> odomPoseAtVis2 = poseBuffer.getSample(m2Time);
        if (odomPoseAtVis1.isEmpty() || odomPoseAtVis2.isEmpty()) {
            return false;
        }
        double odomDiff1To2 = odomPoseAtVis1.get().getTranslation().getDistance(odomPoseAtVis2.get().getTranslation());
        double visionDiff1To2 = m1Pose.getTranslation().getDistance(m2Pose.getTranslation());
        double diff = Math.abs(odomDiff1To2 - visionDiff1To2);
        return Math.abs(diff) <= Constants.VISION_CONSISTANCY_THRESHOLD2;
    }
}
