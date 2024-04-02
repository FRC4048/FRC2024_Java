package frc.robot.utils;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.math.VectorUtils;
import frc.robot.utils.math.VelocityVector;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AutoAlignment {
    private final static HashMap<Alignable, BiFunction<Double, Double, Rotation2d>> positionAngleMap = new HashMap<>(Map.of(
            Alignable.AMP, (x, y) -> new Rotation2d(Math.PI / 2),
            Alignable.SPEAKER, AutoAlignment::calcRobotRotationFaceSpeaker
    ));
    private final static HashMap<Alignable, BiFunction<Double, Translation3d, VelocityVector>> positionYawMap = new HashMap<>(Map.of(
            Alignable.AMP, (aDouble, translation3d) -> new VelocityVector(10.5, Rotation2d.fromDegrees(Ramp.encoderToAngle(Constants.AMP_RAMP_ENC_VALUE))),
            Alignable.SPEAKER, AutoAlignment::calcRampAngle
    ));

    /**
     * Calculates the desired ramp angle given a constant speed {@link Constants#SHOOTER_VELOCITY},
     * and the delta distance in the xy plane and z plane
     *
     * @param x the distance (unsigned) from the speaker on the xy plane
     * @param z the height between the top of the ramp and the middle of the speaker opening
     * @return a {@link Rotation2d} from the ground to the ramp representing the desired angle for shooting
     * @see #calcRampAngle(double, double, double, double)
     */
    @Deprecated
    private static Rotation2d calcRampAngle(double x, double z, double vel) {
        VelocityVector velocityVector = VectorUtils.fromVelAndDist(vel, x, z, 1.0 / 2);
        return (velocityVector == null) ? new Rotation2d(0) : velocityVector.getAngle();
    }

    /**
     * Calculates the desired ramp angle for shooting into the speaker given an initial velocity, and the robots position
     *
     * @param robotVelocityX the velocity of the robot in the x direction
     * @param robotX         the robot's center x position
     * @param robotY         the robot's center y position
     * @param robotZ         distance robot is off the ground (include base of robot)
     * @return a {@link Rotation2d} from the ground to the ramp representing the desired angle for shooting
     */
    private static VelocityVector calcRampAngle(double robotVelocityX, double robotX, double robotY, double robotZ) {
        return VectorUtils.fromDestAndCompoundVel(Constants.SHOOTER_VELOCITY, robotX, robotY, robotZ, robotVelocityX, Alignable.SPEAKER.getX(), Alignable.SPEAKER.getY(), Alignable.SPEAKER.getZ());
    }

    /**
     * Calculates the desired ramp angle for shooting into the speaker given an initial velocity, and the robots position
     *
     * @param robotVelocityX velocity the robo is moving in the x direction
     * @param pose3d         a 3 dimensional pose representing the robots center x position,
     *                       center y position, and z distance from the ground (include base of robot)
     * @return a {@link Rotation2d} from the ground to the ramp representing the desired angle for shooting
     */
    private static VelocityVector calcRampAngle(double robotVelocityX, Translation3d pose3d) {
        return calcRampAngle(robotVelocityX, pose3d.getX(), pose3d.getY(), pose3d.getZ());
    }

    /**
     * Uses basic trig to calculate the desired angle to face the speaker given distance in both x and y directions
     *
     * @param x distance of speaker from robot (robot x - speaker x)
     * @param y distance of speaker from robot (robot y - speaker y)
     * @return the desired rotation of the robot, so it can score in the speaker.
     */
    private static Rotation2d calcRobotRotationFaceSpeaker(double x, double y) {
        return new Rotation2d(RobotContainer.isRedAlliance() ? 0 : Math.PI).plus(new Rotation2d(Math.atan(y / x)));
    }

    public static double calcTurnSpeed(Alignable alignable, Rotation2d currentAngle, double x, double y, PIDController controller) {
        double targetAngle = getAngle(alignable, x, y).getDegrees();
        if (Math.abs(currentAngle.getDegrees() - targetAngle) < Constants.AUTO_ALIGN_THRESHOLD) {
            return 0;
        }
        double clamp = MathUtil.clamp(controller.calculate(currentAngle.getDegrees(), targetAngle), -1 * Constants.MAX_AUTO_ALIGN_SPEED, Constants.MAX_AUTO_ALIGN_SPEED);
        if (Constants.SWERVE_DEBUG) {
            SmartDashboard.putNumber("TURN_PID", clamp);
        }
        return clamp;
    }

    public static double calcTurnSpeed(Alignable alignable, Pose2d currentPos, PIDController controller) {
        return calcTurnSpeed(alignable, currentPos.getRotation(), currentPos.getX(), currentPos.getY(), controller);
    }

    public static double angleFromTarget(Alignable alignable, Rotation2d currentAngle, double x, double y) {
        return getAngle(alignable, x, y).minus(currentAngle).getDegrees();
    }

    public static double angleFromTarget(Alignable alignable, Pose2d currentPose) {
        return angleFromTarget(alignable, currentPose.getRotation(), currentPose.getX(), currentPose.getY());
    }

    public static Rotation2d getAngle(Alignable alignable, double x, double y) {
        if (isInvalidAngle(alignable)) return new Rotation2d();
        BiFunction<Double, Double, Rotation2d> function = positionAngleMap.get(alignable);
        if (isInvalidAngleFunction(function)) return new Rotation2d();
        return function.apply(x - alignable.getX(), y - alignable.getY());
    }


    /**
     * @param alignable      what we are aligning to
     * @param x              position of robot on the x-axis
     * @param y              position of robot on the y-axis
     * @param z              position of the robot on y z-axis
     * @param drivetrainVelX velocity the robo is moving in the x direction
     * @return the desired {@link Rotation2d} of the ramp from the ground
     */
    public static VelocityVector getYaw(Alignable alignable, double x, double y, double z, double drivetrainVelX) {
        if (isInvalidAngle(alignable)) return new VelocityVector(0,new Rotation2d());
        BiFunction<Double, Translation3d, VelocityVector> function = positionYawMap.get(alignable);
        if (isInvalidYawFunction(function)) return new VelocityVector(0, new Rotation2d());
        return function.apply(drivetrainVelX, new Translation3d(x, y, z));
    }

    /**
     * @param alignable      what we are aligning to
     * @param pose3d         ramp position in 3d space (use robot position plus height of ramp, DO NOT manually account for RAMP_X_OFFSET)
     * @param drivetrainVelX velocity the robo is moving in the x direction
     * @return the desired {@link Rotation2d} of the ramp from the ground
     */
    public static VelocityVector getYaw(Alignable alignable, Translation3d pose3d, double drivetrainVelX) {
        return getYaw(alignable, pose3d.getX(), pose3d.getY(), pose3d.getZ(), drivetrainVelX);
    }

    private static boolean isInvalidYawFunction(BiFunction<Double, Translation3d, VelocityVector> function) {
        if (function == null) {
            DriverStation.reportError("Alignable Not in PositionYawMap", true);
            return true;
        }
        return false;
    }

    private static boolean isInvalidAngleFunction(BiFunction<Double, Double, Rotation2d> function) {
        if (function == null) {
            DriverStation.reportError("Alignable Not in PositionAngleMap", true);
            return true;
        }
        return false;
    }

    private static boolean isInvalidAngle(Alignable alignable) {
        if (alignable == null) {
            DriverStation.reportError("Alignable Can not be Null", true);
            return true;
        }
        return false;
    }
}
