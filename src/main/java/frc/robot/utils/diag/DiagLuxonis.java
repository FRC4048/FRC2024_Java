package frc.robot.utils.diag;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.constants.Constants;

public class DiagLuxonis extends DiagBoolean{
    private final DoubleSubscriber subscriber;
    public DiagLuxonis(String title, String name){
        super(title,name);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("LUXONIS");
        subscriber = table.getDoubleTopic("Prob").subscribe(0);
    }
    @Override
    protected boolean getValue() {
        return subscriber.get() > Constants.PIECE_THRESHOLD;
    }
}
