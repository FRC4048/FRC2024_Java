package frc.robot.subsystems.swervev3;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import frc.robot.subsystems.swervev3.bags.OdometryMeasurement;
import frc.robot.subsystems.swervev3.bags.VisionMeasurement;

import java.util.LinkedList;
import java.util.Queue;

public class PoseManager {
    private final TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer;
    private final SwerveDrivePoseEstimator poseEstimator;
    protected final Queue<VisionMeasurement> visionMeasurementQueue = new LinkedList<>();

    public PoseManager(PoseDivation PoseDivation, SwerveDriveKinematics kinematics, OdometryMeasurement initialOdom, TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer) {
        this.poseEstimator = new SwerveDrivePoseEstimator(kinematics,
                Rotation2d.fromDegrees(initialOdom.gyroValueDeg()),
                initialOdom.modulePosition(),
                new Pose2d(),
                PoseDivation.getWheelStd(),
                PoseDivation.getVisionStd()
        );
        this.estimatedPoseBuffer = estimatedPoseBuffer;
    }

    public PoseManager(Vector<N3> wheelStd, Vector<N3> visionStd, SwerveDriveKinematics kinematics, OdometryMeasurement initialOdom, TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer) {
        this(new PoseDivation(wheelStd, visionStd), kinematics, initialOdom, estimatedPoseBuffer);
    }

    public void addOdomMeasurement(OdometryMeasurement m, long timestamp){
        Pose2d pose = poseEstimator.update(Rotation2d.fromDegrees(m.gyroValueDeg()), m.modulePosition());
        estimatedPoseBuffer.addSample(timestamp, pose);
    }
    public void registerVisionMeasurement(VisionMeasurement measurement){
        visionMeasurementQueue.add(measurement);
        processQueue();
    }

    //override for filtering
    protected void processQueue() {
        VisionMeasurement m = visionMeasurementQueue.poll();
        while (m != null){
            addVisionMeasurement(m);
            m = visionMeasurementQueue.poll();
        }
    }

    protected void addVisionMeasurement(VisionMeasurement measurement){
        poseEstimator.addVisionMeasurement(measurement.measurement(), measurement.timeOfMeasurement());
    }


    public void resetPose(OdometryMeasurement m, Translation2d initialPose){
        poseEstimator.resetPosition(Rotation2d.fromDegrees(m.gyroValueDeg()), m.modulePosition(),  new Pose2d(initialPose, Rotation2d.fromDegrees(m.gyroValueDeg())));
    }

    public TimeInterpolatableBuffer<Pose2d> getPoseBuffer() {
        return estimatedPoseBuffer;
    }

    public Pose2d getEstimatedPosition() {
        return poseEstimator.getEstimatedPosition();
    }
}
