// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagAprilTags;
import frc.robot.utils.diag.DiagLimelight;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Vision extends SubsystemBase {
  /** Creates a new Limelight. */
  private final NetworkTable table;
  private final NetworkTableEntry tv;
  private final NetworkTableEntry tx;
  private final NetworkTableEntry ty;
  private int noPieceSeenCounter;
  private boolean pieceSeen;
  private double x;
  private double y;

  
  public Vision() {
    table = NetworkTableInstance.getDefault().getTable("limelight");
    tv = table.getEntry("tv");
    tx = table.getEntry("tx");
    ty = table.getEntry("ty");
    x = tx.getDouble(0.0);
    y = ty.getDouble(0.0);
    pieceSeen = (tv.getDouble(-1) == 1);
    Robot.getDiagnostics().addDiagnosable(new DiagLimelight("Vision", "Piece Seen"));
    Robot.getDiagnostics().addDiagnosable(new DiagAprilTags("Vision", "Apriltag Seen"));
  }

  /**
   * 
   * @return whether or not the piece has not been seen for a number of cycles
   */
  public boolean isPieceSeen() {
    return pieceSeen;
  }

  /**
   * @return The pieces' x offset angle in degrees and 0.0 if the piece isn't seen
   */
  public double getPieceOffestAngleX() {
    return x;
  }

  /**
   * @return The pieces' y offset angle in degrees and 0.0 if the piece isn't seen
   */
  public double getPieceOffestAngleY() {
    return y;
  }

  @Override
  public void periodic() {
    if(tv.getDouble(0) == 0) {
      noPieceSeenCounter++;
      if(noPieceSeenCounter >= Constants.FEEDER_PIECE_NOT_SEEN_COUNTER) {
        x = 0.0;
        y = 0.0;
        pieceSeen = false;
      }
    }
    else {
      noPieceSeenCounter = 0;
      x = tx.getDouble(0.0);
      y = ty.getDouble(0.0);
      pieceSeen = (tv.getDouble(-1) == 1);
    }

    if(Constants.VISION_DEBUG) {
      SmartShuffleboard.put("Limeight", "tv", tv.getDouble(-1));
      SmartShuffleboard.put("Limeight", "tx", tx.getDouble(0.0));
      SmartShuffleboard.put("Limeight", "ty", ty.getDouble(0.0));
    }
  }
}
