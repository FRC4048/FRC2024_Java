package frc.robot.utils;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.utils.math.RangeUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AutoAlignment {
    private final static HashMap<Alignable, BiFunction<Double, Double, Rotation2d>> positionAngleMap = new HashMap<>(Map.of(
       Alignable.AMP, (x,y)-> new Rotation2d(Math.PI),
       Alignable.SPEAKER,(x,y)-> new Rotation2d(RobotContainer.isRedAlliance() ? 0 : Math.PI).plus(new Rotation2d(Math.atan(y/x))
    )));
    private final static PIDController turnController = new PIDController(Constants.ALIGNABLE_PID_P,Constants.ALIGNABLE_PID_I,Constants.ALIGNABLE_PID_D);
    static {
        turnController.enableContinuousInput(-180, 180);
    }
    public static double calcTurnSpeed(Alignable alignable, Rotation2d currentAngle, double x, double y){
        double targetAngle = getAngle(alignable,x,y).getDegrees();
        if (Math.abs(currentAngle.getDegrees() - targetAngle) < Constants.AUTO_ALIGN_THRESHOLD){
            return 0;
        }
        double clamp = RangeUtils.map(turnController.calculate(currentAngle.getDegrees(), targetAngle), -180, 180,-1,1);
        if (Constants.SWERVE_DEBUG) SmartDashboard.putNumber("TURN_PID",clamp);
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
    public static void resetPid(){
        turnController.reset();
    }
    public static Rotation2d getAngle(Alignable alignable, double x, double y) {
        BiFunction<Double, Double, Rotation2d> function = positionAngleMap.get(alignable);
        if (function == null){
            DriverStation.reportError("Alignable Not Found",true);
            return new Rotation2d();
        }
        return function.apply(x - alignable.getX(), y - alignable.getY());
    }
}
