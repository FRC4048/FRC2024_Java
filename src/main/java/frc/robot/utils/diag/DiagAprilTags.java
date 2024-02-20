package frc.robot.utils.diag;

import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

/**
 * A diagnostics class for digital encoder. The diagnostics will turn green once the encoder has traveled at least a given
 * distance from its initial position (measured at initialization or after a reset)
 */
public class DiagAprilTags extends DiagBoolean {
    private final DoubleArraySubscriber subscriber;

    /**
     * Constructor
     * @param name            - the name of the unit. Will be used on the Shuffleboard
     */
    public DiagAprilTags(String title, String name) {
        super(title, name);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("ROS");
        subscriber = table.getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1});
    }

    @Override
    protected boolean getValue() {
        double[] pastValues = subscriber.get();
        return pastValues[0] != -1 && pastValues[1] != -1 && pastValues[2] != -1;
    }
}
