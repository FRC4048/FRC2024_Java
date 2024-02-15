package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Alignable;
import frc.robot.Constants;

public class AutoAlignment {
    public static double calcTurnSpeed(Alignable alignable, Rotation2d currentAngle, double x, double y){
        double diff = angleFromTarget(alignable,currentAngle,x,y);
        if (Math.abs(diff) < Constants.AUTO_ALIGN_THRESHOLD) return 0;
        return (Math.abs(diff)) / (360) * Math.signum(diff);
    }
    public static double calcTurnSpeed(Alignable alignable, Pose2d currentPos){
        return calcTurnSpeed(alignable,currentPos.getRotation(),currentPos.getX(),currentPos.getY());
    }
    public static double angleFromTarget(Alignable alignable, Rotation2d currentAngle, double x, double y){
        return getAngle(alignable,x,y).minus(currentAngle).getDegrees();
    }
    public static double angleFromTarget(Alignable alignable,Pose2d currentPose){
        return angleFromTarget(alignable,currentPose.getRotation(),currentPose.getX(),currentPose.getY());
    }
    public static Rotation2d getAngle(Alignable alignable, double x, double y) {
        return alignable.getAngleFromDistFunc().apply(x - alignable.getX(), y - alignable.getY());
    }
}
