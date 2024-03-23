package frc.robot.constants;

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

    public TrapPositionList getTag() {
        double tagId = subscriber.get(0);
        if (tagId == 0) {
            return null;
        }
        return TrapPositionList.getTag((int) subscriber.get());
    }

    public Command planPath() {
        TrapPositionList tag = getTag();
        if (tag == null) {
            return new InstantCommand();
        } else {
            return PathPlannerUtils.pathToPose(tag.getTrapPosition(), 0);
        }

    }
}
