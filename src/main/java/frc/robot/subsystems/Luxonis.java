package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Luxonis extends SubsystemBase {

    NetworkTableEntry xEntry;
    NetworkTableEntry yEntry;
    NetworkTableEntry zEntry;
    NetworkTableEntry fpsEntry;
    NetworkTableEntry probEntry;
    
    public Luxonis() {
        NetworkTable cameraTable = NetworkTableInstance.getDefault().getTable("Luxonis");
        xEntry = cameraTable.getEntry("x");
        yEntry = cameraTable.getEntry("y");
        zEntry = cameraTable.getEntry("z");
        fpsEntry = cameraTable.getEntry("fps");
        probEntry = cameraTable.getEntry("prob");
    }

    @Override
    public void periodic() {
        SmartShuffleboard.put("luxonis", "X", xEntry.getNumber(0));
        SmartShuffleboard.put("luxonis", "Y", xEntry.getNumber(0));
        SmartShuffleboard.put("luxonis", "Z", xEntry.getNumber(0));
        SmartShuffleboard.put("luxonis", "FPS", xEntry.getNumber(0));
        SmartShuffleboard.put("luxonis", "Prob", xEntry.getNumber(0));
    }
}
