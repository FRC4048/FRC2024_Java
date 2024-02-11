package frc.robot;

public class Constants2023 extends GameConstants {
    //DEBUG
    public static final boolean SHOOTER_DEBUG = false;
    public static final boolean RAMP_DEBUG = false;
    public static final boolean SWERVE_DEBUG = false;
    public static final boolean FEEDER_DEBUG = false;
    //RAMP
    public static final double RAMP_PID_P = 1.00;
    public static final double RAMP_PID_I = 0.00;
    public static final double RAMP_PID_D = 0.00;
    public static final double RAMP_PID_FF = 1.00;
    public static final double RAMP_ERROR_IZONE = 0.00;
    public static final double RAMP_POS = 0.0;
    public static final int RAMP_ID = 45;

    //Shooter
    public static final int SHOOTER_MOTOR_ID_1 = 4400; //needs to be changed
    public static final int SHOOTER_MOTOR_ID_2 = 4500; //needs to be changed

    public static final int SHOOTER_SENSOR_ID_1 = 0;
    public static final int SHOOTER_SENSOR_ID_2 = 1;

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

    public static final boolean FIELD_RELATIVE = true;

    //S = STEER, D = DRIVE, Drivetrain ConstantsS
    public static final SwerveModuleProfile SWERVE_MODULE_PROFILE = SwerveModuleProfile.MK4I;
    public static final int DRIVE_FRONT_RIGHT_S = 49;
    public static final int DRIVE_FRONT_RIGHT_D = 40;
    public static final int DRIVE_BACK_RIGHT_S = 43;
    public static final int DRIVE_BACK_RIGHT_D = 46;
    public static final int DRIVE_FRONT_LEFT_S = 51;
    public static final int DRIVE_FRONT_LEFT_D = 50;
    public static final int DRIVE_BACK_LEFT_S = 34;
    public static final int DRIVE_BACK_LEFT_D = 31;

    public static final int DRIVE_CANCODER_FRONT_RIGHT = 59;
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

    public static final double MAX_VELOCITY = 4.0; // 4 meters per second
    public static final double MAX_ANGULAR_SPEED = Math.PI * 6; // 1 rotation per second
    public static final double ROBOT_WIDTH = 0.5969;
    public static final double ROBOT_LENGTH = 0.5969;

    public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 54.35;
    public static final double FRONT_LEFT_ABS_ENCODER_ZERO = 339.5;
    public static final double BACK_LEFT_ABS_ENCODER_ZERO = 18.72;
    public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = 133.3;

    //Feeder
    public static final double FEEDER_MOTOR_SPEED = 0.5;
    public static final int FEEDER_MOTOR_ID = 11;
    public static final int FEEDER_SENSOR_ID = 0;

}
