package frc.robot.utils.diag;

import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class DiagLuxonis extends DiagBoolean{
    private final DoubleSubscriber subscriber;
    private boolean SeenTrue = false;
    private boolean SeenFalse = false;
    public DiagLuxonis(String title, String name){
        super(title,name);
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("LUXONIS");
        subscriber = table.getDoubleTopic("Prob").subscribe(0);
    }
    protected boolean getValue(String type){
        if (type == "greater" && subscriber.get()>0.8){
            return true;
        } else if (type == "less" && subscriber.get()<0.6){
            return true;
        } else {
            return false;
        }
    }
    @Override
    boolean getDiagResult() {
        boolean currentGreaterValue = getValue("greater");
        boolean currentLesserValue = getValue("lesser");
        // Set the value for the state - whether the switch is pressed or not
        if (currentGreaterValue) {
            SeenTrue = true;
        } else if(currentLesserValue) {
            SeenFalse = true;
        } 
        return SeenTrue && SeenFalse;
    }
}
