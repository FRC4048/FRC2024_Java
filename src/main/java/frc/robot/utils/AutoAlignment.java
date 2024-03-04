package frc.robot.utils;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
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
            Alignable.SPEAKER, (x, y) -> new Rotation2d(RobotContainer.isRedAlliance() ? 0 : Math.PI).plus(new Rotation2d(Math.atan(y / x)))
    ));
    private final static HashMap<Alignable, BiFunction<Double, Double, Rotation2d>> positionYawMap = new HashMap<>(Map.of(
            Alignable.AMP, (x, y) -> Rotation2d.fromDegrees(Ramp.encoderToAngle(Constants.AMP_RAMP_ENC_VALUE)),
            Alignable.SPEAKER, (x, z) -> {
                VelocityVector velocityVector = VectorUtils.fromVelAndDist(Constants.SHOOTER_VELOCITY, x, z, true);
                return (velocityVector == null) ? new Rotation2d(0) : velocityVector.getAngle();
            })
    );

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
        if (isInvalidFunction(function)) return new Rotation2d();
        return function.apply(x - alignable.getX(), y - alignable.getY());
    }

    public static Rotation2d getYaw(Alignable alignable, double x, double y, double z) {
        if (isInvalidAngle(alignable)) return new Rotation2d();
        BiFunction<Double, Double, Rotation2d> function = positionYawMap.get(alignable);
        if (isInvalidFunction(function)) return new Rotation2d();
        double deltaX = x - alignable.getX();
        double deltaY = y - alignable.getY();
        double dist = Math.hypot(deltaX, deltaY);
        return function.apply(dist, alignable.getZ() - z);
    }

    public static Rotation2d getYaw(Alignable alignable, Translation2d pose, double z) {
        Rotation2d yaw = getYaw(alignable, pose.getX(), pose.getY(), z);
        double clamp = MathUtil.clamp(yaw.getDegrees(), Constants.RAMP_MIN_ANGLE, Constants.RAMP_MAX_ANGLE);
        return Rotation2d.fromDegrees(clamp);
    }

    private static boolean isInvalidFunction(BiFunction<Double, Double, Rotation2d> function) {
        if (function == null) {
            DriverStation.reportError("Alignable Not in PositionYawMap", true);
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
