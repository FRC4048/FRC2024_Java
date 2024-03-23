package frc.robot.constants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.utils.PathPlannerUtils;
import frc.robot.utils.TrapPositionList;

public class AprilTag {
    private NetworkTableInstance inst;
    private NetworkTable table;
    private DoubleSubscriber subscriber;

    public AprilTag() {
        inst = NetworkTableInstance.getDefault();
        table = inst.getTable("ROS");
        subscriber = table.getDoubleTopic("apriltag_id").subscribe(0);
    }
    public TrapPositionList getTag(){
        int tagId = (int) subscriber.get(0);
        if (tagId == 0) {
            return null;
        }
        return TrapPositionList.getTag((int)subscriber.getAsDouble());
    }
    public Command planPath(){
        TrapPositionList tag = getTag();
        if (tag == null) {
            return new InstantCommand();
        }return PathPlannerUtils.pathToPose(tag.getTrapPosition(), 0);
    }

}
