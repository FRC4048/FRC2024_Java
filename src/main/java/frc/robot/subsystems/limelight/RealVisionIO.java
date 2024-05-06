package frc.robot.subsystems.limelight;

import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagAprilTags;
import frc.robot.utils.diag.DiagLimelight;

import java.util.Map;

public class RealVisionIO implements VisionIO {
    private final NetworkTableEntry tv;
    private final NetworkTableEntry tx;
    private final NetworkTableEntry ty;

    public RealVisionIO() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        tv = table.getEntry("tv");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        Robot.getDiagnostics().addDiagnosable(new DiagLimelight("Vision", "Piece Seen"));
        Robot.getDiagnostics().addDiagnosable(new DiagAprilTags("Vision", "Apriltag Seen"));

        HttpCamera limelightFeed = new HttpCamera("limelight", "http://" + Constants.LIMELIGHT_IP_ADDRESS + ":5800/stream.mjpg");
        ShuffleboardTab dashboardTab = Shuffleboard.getTab("Driver");
        dashboardTab.add("Limelight feed", limelightFeed).withSize(6, 4).withPosition(2, 0).withProperties(Map.of("Show Crosshair", false, "Show Controls", false));
    }

    @Override
    public void updateInputs(VisionInputs inputs) {
        inputs.tv = tv.getDouble(0);
        inputs.tx = tx.getDouble(0);
        inputs.ty = ty.getDouble(0);
    }
}
