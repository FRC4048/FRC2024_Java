// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.utils;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants
{
    public static final double ArmSeperationDistance = 5.00; 
    public static final double INTAKE_SPEED = 1.00;
    public static final double OUTTAKE_SPEED = 1.00;
    public static final double CLIMBER_SPEED = 0.10;
    public static final int CONTROLLER_ID = 1;
    public static final int INTAKE_MOTOR1_ID = 4;
    public static final int INTAKE_MOTOR2_ID = 5;
    public static final int OUTTAKE_MOTOR1_ID = 6;
    public static final int OUTTAKE_MOTOR2_ID = 7;
    public static final int CLIMBER_MOTOR1_ID = 44;
    public static final int CLIMBER_MOTOR2_ID = 45;
    public static final double BALANCE_kP = 1;
    public static final double BALANCE_kTi = 1;
    public static final double Balance_KTd = 1;
    public static final double BALANCE_LOW_SPEED=0.30;
    public static final double BALANCE_HIGH_SPEED=0.50;
    public static final double BALANCE_THRESH=30;
}
