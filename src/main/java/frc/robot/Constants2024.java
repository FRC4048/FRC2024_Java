package frc.robot;

public class Constants2024 extends GameConstants {
    public static final double RAMP_PID_P = 1.00;
    public static final double RAMP_PID_I = 0.00;
    public static final double RAMP_PID_D = 0.00;
    public static final double RAMP_PID_FF = 1.00;
    public static final double RAMP_ERROR_IZONE = 0.00;
    public static final double RAMP_POS = 0.0;
    public static final int RAMP_ID = 47;

    //Shooter
    public static final int SHOOTER_MOTOR_ID_1 = 44; //needs to be changed
    public static final int SHOOTER_MOTOR_ID_2 = 45; //needs to be changed

    public static final int SHOOTER_SENSOR_ID_1 = 2; //needs to be changed
    public static final int SHOOTER_SENSOR_ID_2 = 1; //needs to be changed

    public static final double SHOOTER_MOTOR_SPEED = 0.75;

    public static final double SHOOTER_MOTOR_1_RPM = 12000;
    public static final double SHOOTER_MOTOR_2_RPM = 12000;

    //Shooter motor PID constants
    public static final double SHOOTER_MOTOR_PID_P = 5e-5;
    public static final double SHOOTER_MOTOR_PID_I = 0;
    public static final double SHOOTER_MOTOR_PID_D = 5e-5;
    public static final double SHOOTER_MOTOR_PID_IZ = 0;
    public static final double SHOOTER_MOTOR_PID_FF = 0.000015;
    public static final double SHOOTER_MOTOR_MAX_OUTPUT = 1;
    public static final double SHOOTER_MOTOR_MIN_OUTPUT = -1;

    public static final boolean FIELD_RELATIVE = false;

    //S = STEER, D = DRIVE, Drivetrain ConstantsS
    public static final int DRIVE_FRONT_RIGHT_S = 27;
    public static final int DRIVE_FRONT_RIGHT_D = 57;
    public static final int DRIVE_BACK_RIGHT_S = 28;
    public static final int DRIVE_BACK_RIGHT_D = 58;
    public static final int DRIVE_FRONT_LEFT_S = 30;
    public static final int DRIVE_FRONT_LEFT_D = 60;
    public static final int DRIVE_BACK_LEFT_S = 29;
    public static final int DRIVE_BACK_LEFT_D = 59;

    public static final int DRIVE_CANCODER_FRONT_RIGHT = 37;
    public static final int DRIVE_CANCODER_BACK_RIGHT = 38;
    public static final int DRIVE_CANCODER_FRONT_LEFT = 40;
    public static final int DRIVE_CANCODER_BACK_LEFT = 39;

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

    public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 190.89;
    public static final double FRONT_LEFT_ABS_ENCODER_ZERO = 260.24;
    public static final double BACK_LEFT_ABS_ENCODER_ZERO = 307.44;
    public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = 119.25;

    //Inverted steer
    public static boolean INVERTED_STEER = false;

    //Feeder
    public static final double FEEDER_MOTOR_SPEED = 0.5;
    public static final int FEEDER_MOTOR_ID = 11;
    public static final int FEEDER_SENSOR_ID = 0;
}
