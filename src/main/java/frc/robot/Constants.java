// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {
    public static class OperatorConstants {
        public static final int kDriverControllerPort = 0;
    }

    //JOYSTICKS
    public static final int LEFT_JOYSICK_ID = 0;
    public static final int RIGHT_JOYSTICK_ID = 1;

    //S = STEER, D = DRIVE, Drivetrain ConstantsS
    public static final int DRIVE_FRONT_RIGHT_S = 40;
    public static final int DRIVE_FRONT_RIGHT_D = 49;
    public static final int DRIVE_BACK_RIGHT_S = 46;
    public static final int DRIVE_BACK_RIGHT_D = 43;
    public static final int DRIVE_FRONT_LEFT_S = 50;
    public static final int DRIVE_FRONT_LEFT_D = 51;
    public static final int DRIVE_BACK_LEFT_S = 31;
    public static final int DRIVE_BACK_LEFT_D = 34;

    public static final int DRIVE_CANCODER_FRONT_RIGHT = 60;
    public static final int DRIVE_CANCODER_BACK_RIGHT = 57;
    public static final int DRIVE_CANCODER_FRONT_LEFT = 56;
    public static final int DRIVE_CANCODER_BACK_LEFT = 58;

    //PID Constants
    public static final double DRIVE_PID_P = 1;
    public static final double DRIVE_PID_I = 0;
    public static final double DRIVE_PID_D = 0;
    public static final double DRIVE_PID_FF_S = 1;
    public static final double DRIVE_PID_FF_V = 2.8;

    public static final double STEER_PID_P = 0.3;
    public static final double STEER_PID_I = 0;
    public static final double STEER_PID_D = 0;
    public static final double STEER_PID_FF_S = 0;//0.2;
    public static final double STEER_PID_FF_V = 0;//0.8;
    public static final double WHEEL_RADIUS = 0.0508;
    public static final double CHASSIS_DRIVE_GEAR_RATIO = 8.142857; // this value should be x:1
    public static final double CHASSIS_STEER_GEAR_RATIO = 12.8; // this value should be x:1

    public static final double MAX_VELOCITY = 4.0; // 4 meters per second
    public static final double MAX_ANGULAR_SPEED = Math.PI * 6; // 1 rotation per second
    public static final double ROBOT_WIDTH = 0.5969;
    public static final double ROBOT_LENGTH = 0.5969;

    public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 218.76;
    public static final double FRONT_LEFT_ABS_ENCODER_ZERO = 9.0;
    public static final double BACK_LEFT_ABS_ENCODER_ZERO = 351.3;
    public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = 299.13;
}
