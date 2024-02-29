package frc.robot.utils;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.GoalEndState;
import com.pathplanner.lib.path.PathConstraints;
import com.pathplanner.lib.path.PathPlannerPath;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;

import java.util.List;

public class PathPlannerUtils {
    private static final PathConstraints defualtPathConstraints =  new PathConstraints(Constants.MAX_VELOCITY,Constants.MAX_VELOCITY,1000,1000);
    public static PathPlannerPath createBasicPath(Pose2d startPose, Pose2d targetPos, double endVelocity){
        List<Translation2d> bezierPoints = PathPlannerPath.bezierFromPoses(
                startPose, targetPos
        );
        PathPlannerPath path = new PathPlannerPath(bezierPoints, defualtPathConstraints, new GoalEndState(endVelocity,targetPos.getRotation(),false));
        path.preventFlipping = true;
        return path;
    }
    public static Command autoFromPath(PathPlannerPath path){
        return AutoBuilder.followPath(path);
    }
}
