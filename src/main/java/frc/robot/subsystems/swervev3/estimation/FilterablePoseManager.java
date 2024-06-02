package frc.robot.subsystems.swervev3.estimation;

import edu.wpi.first.math.Vector;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.interpolation.TimeInterpolatableBuffer;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.numbers.N3;
import frc.robot.subsystems.swervev3.bags.OdometryMeasurement;
import frc.robot.subsystems.swervev3.bags.VisionMeasurement;
import frc.robot.subsystems.swervev3.vision.FilterResult;
import frc.robot.subsystems.swervev3.vision.PoseDivation;
import frc.robot.subsystems.swervev3.vision.VisionFilter;
import org.littletonrobotics.junction.Logger;

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FilterablePoseManager extends PoseManager {
    private final VisionFilter filter;

    public FilterablePoseManager(PoseDivation PoseDivation, SwerveDriveKinematics kinematics, OdometryMeasurement initialOdom, TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer, VisionFilter filter) {
        super(PoseDivation, kinematics, initialOdom, estimatedPoseBuffer);
        this.filter = filter;
    }

    public FilterablePoseManager(Vector<N3> wheelStd, Vector<N3> visionStd, SwerveDriveKinematics kinematics, OdometryMeasurement initialOdom, TimeInterpolatableBuffer<Pose2d> estimatedPoseBuffer, VisionFilter filter) {
        this(new PoseDivation(wheelStd, visionStd), kinematics, initialOdom, estimatedPoseBuffer, filter);
    }

    @Override
    protected void processQueue() {
        LinkedHashMap<VisionMeasurement, FilterResult> filter1 = filter.filter(visionMeasurementQueue);
        visionMeasurementQueue.clear();
        AtomicInteger numRejected = new AtomicInteger();
        filter1.forEach((v, r) -> {
            switch (r){
                case ACCEPTED -> addVisionMeasurement(v);
                case NOT_PROCESSED -> visionMeasurementQueue.add(v);
                case REJECTED -> numRejected.getAndIncrement();
            }
        });
        Logger.recordOutput("rejectedMeasurementsCount", numRejected.get());
    }
}
