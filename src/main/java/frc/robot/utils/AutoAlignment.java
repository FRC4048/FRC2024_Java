package frc.robot.utils;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.constants.Constants;
import frc.robot.utils.math.RangeUtils;

public class AutoAlignment {
    private final static PIDController turnController = new PIDController(0.7,0,0.06);
    static {
        turnController.enableContinuousInput(-180,180);
    }
    public static double calcTurnSpeed(Alignable alignable, Rotation2d currentAngle, double x, double y){
        double targetAngle = getAngle(alignable,x,y).getDegrees();
        if (Math.abs(currentAngle.getDegrees()-targetAngle) < Constants.AUTO_ALIGN_THRESHOLD){
            return 0;
        }
        double clamp = RangeUtils.map(turnController.calculate(currentAngle.getDegrees(), targetAngle), -180, 180,-1,1);
        SmartDashboard.putNumber("TURN_PID",clamp);
        return clamp;
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
    public static void resetPid(){
        turnController.reset();
    }
}
