// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.vision;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Vision extends SubsystemBase {
  /** Creates a new Limelight. */
  private final VisionIO visionIO;
  private final VisionInputs inputs = new VisionInputs();
  
  public Vision(VisionIO io) {
    visionIO = io;
  }

  /**
   * @return The piece's x offset angle in degrees and 0.0 if the piece isn't seen
   */
  public double getPieceOffestAngleX() {
    return inputs.tx;
  }

  /**
   * @return The piece's y offset angle in degrees and 0.0 if the piece isn't seen
   */
  public double getPieceOffestAngleY() {
    return inputs.ty;
  }
  public boolean isPieceSeen(){
    return inputs.tv != 0;
  }

  @Override
  public void periodic() {
    visionIO.updateInputs(inputs);
    org.littletonrobotics.junction.Logger.processInputs("VisionInputs", inputs);
  }
}
