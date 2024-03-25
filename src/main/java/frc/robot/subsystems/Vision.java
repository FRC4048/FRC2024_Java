// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagAprilTags;
import frc.robot.utils.diag.DiagLimelight;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import java.util.Map;

public class Vision extends SubsystemBase {
  /** Creates a new Limelight. */
  private final NetworkTable table;
  private final NetworkTableEntry tv;
  private final NetworkTableEntry tx;
  private final NetworkTableEntry ty;

  
  public Vision() {
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tv = table.getEntry("tv");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    Robot.getDiagnostics().addDiagnosable(new DiagLimelight("Vision", "Piece Seen"));
    Robot.getDiagnostics().addDiagnosable(new DiagAprilTags("Vision", "Apriltag Seen"));

    HttpCamera limelightFeed = new HttpCamera("limelight", "http://" + Constants.LIMELIGHT_IP_ADDRESS + ":5800/stream.mjpg");
    ShuffleboardTab dashboardTab = Shuffleboard.getTab("Driver");
    dashboardTab.add("Limelight feed", limelightFeed).withSize(6,4).withPosition(2, 0).withProperties(Map.of("Show Crosshair", false, "Show Controls", false));
  }

  /**
   * @return The piece's x offset angle in degrees and 0.0 if the piece isn't seen
   */
  public double getPieceOffestAngleX() {
    return tx.getDouble(0);
  }

  /**
   * @return The piece's y offset angle in degrees and 0.0 if the piece isn't seen
   */
  public double getPieceOffestAngleY() {
    return ty.getDouble(0);
  }
  public double isPieceSeen(){
    return tv.getDouble(0);
  }

  @Override
  public void periodic() {
    if(Constants.VISION_DEBUG) {
      SmartShuffleboard.put("Vision", "tv", tv.getDouble(-1));
      SmartShuffleboard.put("Vision", "tx", tx.getDouble(0.0));
      SmartShuffleboard.put("Vision", "ty", ty.getDouble(0.0));
    }
  }
}
