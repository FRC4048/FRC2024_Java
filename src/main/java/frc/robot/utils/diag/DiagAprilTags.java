package frc.robot.utils.diag;

import edu.wpi.first.networktables.DoubleArraySubscriber;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

/**
 * A diagnostics class for digital encoder. The diagnostics will turn green once the encoder has traveled at least a given
 * distance from its initial position (measured at initialization or after a reset)
 */
public class DiagAprilTags implements Diagnosable {
    private DoubleArraySubscriber subscriber;
    private String title;
    private String name;
    private GenericEntry networkTableEntry;

    /**
     * Constructor
     * @param name            - the name of the unit. Will be used on the Shuffleboard
     */
    public DiagAprilTags(String title, String name) {
        this.title = title;
        this.name = name;
        reset();
    }

    @Override
    public void refresh() {
        if (networkTableEntry != null) {
            networkTableEntry.setBoolean(getDiagResult());
        }
    }

    @Override
    public void reset() {
        subscriber = NetworkTableInstance.getDefault().getTable("ROS").getDoubleArrayTopic("Pos").subscribe(new double[]{-1,-1,-1});
    }

    @Override
    public void setShuffleBoardTab(ShuffleboardTab shuffleBoardTab, int width, int height) {
        networkTableEntry = shuffleBoardTab.getLayout(title, BuiltInLayouts.kList).withSize(width, height).add(name, false).getEntry();
    }

    boolean getDiagResult() {
        double[] pastValues = subscriber.get();
        return pastValues[0] != -1 && pastValues[1] != -1 && pastValues[2] != -1;
    }

}
