package frc.robot.constants;

import frc.robot.swervev2.SwerveModuleProfile;

public class Constants2023 extends GameConstants {
    //RAMP
    public static final double RAMP_PID_P = 1.00;
    public static final double RAMP_PID_I = 0.00;
    public static final double RAMP_PID_D = 0.00;
    public static final double RAMP_PID_FF = 1.00;
    public static final double RAMP_ERROR_IZONE = 0.00;
    public static final double RAMP_POS = 0.0;
    public static final int RAMP_ID = 45;
    public static final double RAMP_MAX_RPM_VELOCITY = 500;
    public static final double RAMP_MAX_RPM_ACCELERATION = 1500;

    //Shooter
    public static final int SHOOTER_MOTOR_ID_1 = 4400; //needs to be changed
    public static final int SHOOTER_MOTOR_ID_2 = 4500; //needs to be changed
  
    public static final int SHOOTER_SENSOR_ID_1 = 0;
    public static final int SHOOTER_SENSOR_ID_2 = 1;

    public static final double SHOOTER_MOTOR_SPEED = 0.75;

    public static final double SHOOTER_MOTOR_1_RPM = 12000;
    public static final double SHOOTER_MOTOR_2_RPM = 12000;
    public static final double SHOOTER_TIME_AFTER_TRIGGER = 0.5;

    //Shooter motor PID constants
    public static final double SHOOTER_MOTOR_PID_P = 5e-5;
    public static final double SHOOTER_MOTOR_PID_I = 0;
    public static final double SHOOTER_MOTOR_PID_D = 5e-5;
    public static final double SHOOTER_MOTOR_PID_IZ = 0;
    public static final double SHOOTER_MOTOR_PID_FF = 0.000015;
    public static final double SHOOTER_MOTOR_MAX_OUTPUT = 1;
    public static final double SHOOTER_MOTOR_MIN_OUTPUT = -1;

    public static final boolean FIELD_RELATIVE = true;

    //S = STEER, D = DRIVE, Drivetrain Constants
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
    public static final double ROBOT_RADIUS =  0.4191;

    public static final double MAX_VELOCITY = 4.0; // 4 meters per second
    public static final double MAX_ANGULAR_SPEED = Math.PI * 6; // 1 rotation per second
    public static final double ROBOT_WIDTH = 0.5969;
    public static final double ROBOT_LENGTH = 0.5969;
    public static final double AUTO_ALIGN_THRESHOLD = 2; //degrees

    public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 54.35;
    public static final double FRONT_LEFT_ABS_ENCODER_ZERO = 339.5;
    public static final double BACK_LEFT_ABS_ENCODER_ZERO = 18.72;
    public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = 133.3;

    //Feeder
    public static final double FEEDER_MOTOR_SPEED = 0.5;
    public static final int FEEDER_MOTOR_ID = 111;
    public static final int FEEDER_SENSOR_ID = 0;

    public static final double ARM_SEPERATION_DISTANCE = 5.00;
    public static final double OUTTAKE_SPEED = 1.00;
    public static final double CLIMBER_SPEED = 0.10;
    public static final int OUTTAKE_MOTOR1_ID = 6;
    public static final int OUTTAKE_MOTOR2_ID = 7;
    public static final int CLIMBER_MOTOR1_ID = 144;
    public static final int CLIMBER_MOTOR2_ID = 145;
    public static final double CLIMBER_BALANCE_kP = 1;
    public static final double CLIMBER_BALANCE_kTi = 1;
    public static final double CLIMBER_Balance_KTd = 1;
    public static final double CLIMBER_BALANCE_LOW_SPEED=0.10;
    public static final double CLIMBER_BALANCE_HIGH_SPEED=0.50;
    public static final double CLIMBER_BALANCE_THRESH=30;
    public static final double CLIMBER_TIMEOUT_S=10;

    //Deployer Constants
    public static final int DEPLOYER_MOTOR_ID = 11;
}
