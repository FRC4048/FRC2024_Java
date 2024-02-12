package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Alignable;

public class AutoAlignment {
    public static double calcTurnSpeed(Alignable alignable, Rotation2d currentAngle, double x, double y){
        double diff = currentAngle.minus(alignable.getAngle(x,y)).getDegrees();
        if (Math.abs(diff) < 2) return 0;
        return (Math.abs(diff)) / (360) * Math.signum(diff);
    }
    public static double calcTurnSpeed(Alignable alignable, Pose2d currentPos){
        return calcTurnSpeed(alignable,currentPos.getRotation(),currentPos.getX(),currentPos.getY());
    }
}
