package frc.robot.utils.diag;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.constants.Constants;

public class DiagLuxonis implements Diagnosable{
    private DoubleSubscriber subscriber;
    private String title;
    private String name;
    private GenericEntry networkTableEntry;

    public DiagLuxonis(String title, String name){
        this.title = title;
        this.name = name;
        reset();
    }
    protected boolean getDiagResult() {
        return subscriber.get() > Constants.PIECE_THRESHOLD;
    }
    @Override
    public void setShuffleBoardTab(ShuffleboardTab shuffleBoardTab, int width, int height) {
        networkTableEntry = shuffleBoardTab.getLayout(title, BuiltInLayouts.kList).withSize(width, height).add(name, false).getEntry();
    }
    @Override
    public void refresh() {
        if (networkTableEntry != null) {
            networkTableEntry.setBoolean(getDiagResult());
        }
    }
    @Override
    public void reset() {
        subscriber = NetworkTableInstance.getDefault().getTable("LUXONIS").getDoubleTopic("Prob").subscribe(0);
    }
}
