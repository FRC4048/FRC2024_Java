package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.utils.diag.DiagGetGamePieceDetection;


public class LuxonisCamera extends SubsystemBase {
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("Luxonis");
    NetworkTableEntry xEntry = table.getEntry("x");
    NetworkTableEntry yEntry = table.getEntry("y");
    NetworkTableEntry zEntry = table.getEntry("z");
    NetworkTableEntry probEntry = table.getEntry("prob");
    NetworkTableEntry fpsEntry = table.getEntry("fps");

    public LuxonisCamera() {
        Robot.getDiagnostics().addDiagnosable(new DiagGetGamePieceDetection("Diagnostics", "Camera", probEntry) {
            
        });
    }

    public double getProb() {
        return probEntry.getDouble(0);
    }

  

    public void periodic() {

    }


}
