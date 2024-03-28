package frc.robot.utils.math;

import edu.wpi.first.math.geometry.*;

public class PoseUtils {
    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose current pose
     * @param vx   x velocity
     * @param vy   y velocity
     * @param time time over which velocity is applied
     * @return Estimated position
     */
    public static Pose2d getFieldEstimatedFuturePose(Pose2d pose, double vx, double vy, double time) {
        return getFieldEstimatedFuturePose(pose, vx, vy, 0.0, time);
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose current pose
     * @param vx   x velocity
     * @param vy   y velocity
     * @param time time over which velocity is applied
     * @return Estimated position
     */
    public static Transform2d getFieldEstimatedFuturePose(Transform2d pose, double vx, double vy, double time) {
        return getFieldEstimatedFuturePose(pose, vx, vy, 0.0, time);
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose current pose
     * @param vx   x velocity
     * @param vy   y velocity
     * @param time time over which velocity is applied
     * @return Estimated position
     */
    public static Translation2d getFieldEstimatedFuturePose(Translation2d pose, double vx, double vy, double time) {
        return pose.plus(new Translation2d(vx * time, vy * time));
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose current pose
     * @param vx   x velocity
     * @param vy   y velocity
     * @param vz   z velocity
     * @param time time over which velocity is applied
     * @return Estimated position
     */
    public static Pose3d getFieldEstimatedFuturePose(Pose3d pose, double vx, double vy, double vz, double time) {
        return getFieldEstimatedFuturePose(pose, vx, vy, vz, 0.0, 0.0, 0.0, time);
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose current pose
     * @param vx   x velocity
     * @param vy   y velocity
     * @param vz   z velocity
     * @param time time over which velocity is applied
     * @return Estimated position
     */
    public static Transform3d getFieldEstimatedFuturePose(Transform3d pose, double vx, double vy, double vz, double time) {
        return getFieldEstimatedFuturePose(pose, vx, vy, vz, 0.0, 0.0, 0.0, time);
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose current pose
     * @param vx   x velocity
     * @param vy   y velocity
     * @param vz   z velocity
     * @param time time over which velocity is applied
     * @return Estimated position
     */
    public static Translation3d getFieldEstimatedFuturePose(Translation3d pose, double vx, double vy, double vz, double time) {
        return pose.plus(new Translation3d(vx * time, vy * time, vz * time));
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose current pose
     * @param vx   x velocity
     * @param vy   y velocity
     * @param vYaw yaw velocity
     * @param time time over which velocity is applied
     * @return Estimated position
     */
    public static Pose2d getFieldEstimatedFuturePose(Pose2d pose, double vx, double vy, double vYaw, double time) {
        return pose.transformBy(new Transform2d(vx * time, vy * time, new Rotation2d(vYaw * time)));
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose current pose
     * @param vx   x velocity
     * @param vy   y velocity
     * @param vYaw yaw velocity
     * @param time time over which velocity is applied
     * @return Estimated position
     */
    public static Transform2d getFieldEstimatedFuturePose(Transform2d pose, double vx, double vy, double vYaw, double time) {
        return pose.plus(new Transform2d(vx * time, vy * time, Rotation2d.fromDegrees(vYaw * time)));
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose   current pose
     * @param vx     x velocity
     * @param vy     y velocity
     * @param vz     z velocity
     * @param vYaw   yaw velocity
     * @param vRoll  roll velocity
     * @param vPitch pitch velocity
     * @param time   time over which velocity is applied
     * @return Estimated position
     */
    public static Pose3d getFieldEstimatedFuturePose(Pose3d pose, double vx, double vy, double vz, double vYaw, double vRoll, double vPitch, double time) {
        return pose.transformBy(new Transform3d(vx * time, vy * time, vz * time, new Rotation3d(vRoll * time, vPitch * time, vYaw * time)));
    }

    /**
     * Estimates a future position based on the current position and velocities
     *
     * @param pose   current pose
     * @param vx     x velocity
     * @param vy     y velocity
     * @param vz     z velocity
     * @param vYaw   yaw velocity
     * @param vRoll  roll velocity
     * @param vPitch pitch velocity
     * @param time   time over which velocity is applied
     * @return Estimated position
     */
    public static Transform3d getFieldEstimatedFuturePose(Transform3d pose, double vx, double vy, double vz, double vYaw, double vRoll, double vPitch, double time) {
        return pose.plus(new Transform3d(vx * time, vy * time, vz * time, new Rotation3d(vRoll * time, vPitch * time, vYaw * time)));
    }
}
