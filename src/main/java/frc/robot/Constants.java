// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  
  //Shooter
  public static final int SHOOTER_MOTOR_ID_1 = 44;
  public static final int SHOOTER_MOTOR_ID_2 = 45;

  public static final int SHOOTER_SENSOR_ID = 0;

  public static final double SHOOTER_MOTOR_SPEED = 0.75;

  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
}
