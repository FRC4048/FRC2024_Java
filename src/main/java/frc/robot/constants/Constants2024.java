package frc.robot.constants;

import frc.robot.swervev2.SwerveModuleProfile;


public class Constants2024 extends GameConstants {
    //AMP 
    public static final int AMP_ID = 5;
    

    //RAMP
    public static final double RAMP_ERROR_RANGE = 0.00;
    public static final double RAMP_ANGLE = 15.0;
    public static final double RAMP_POS = 0.0;
    public static final int RAMP_ID = 47;
    public static final double RAMP_MAX_RPM_ACCELERATION = 3000;
    public static final double RESET_RAMP_SPEED = -0.3; //assuming positive is forward, also needs to be refined do the real robot

    //Servo
    public static final int RIGHT_SERVO_ENGAGED = 0;
    public static final int RIGHT_SERVO_DISENGAGED = 180;
    public static final int LEFT_SERVO_ENGAGED = 0;
    public static final int LEFT_SERVO_DISENGAGED = 180;
    public static final int LEFT_SERVO_ID = 9;
    public static final int RIGHT_SERVO_ID = 8;

    //Shooter
    public static final int SHOOTER_MOTOR_LEFT = 55; //needs to be changed
    public static final int SHOOTER_MOTOR_RIGHT = 56; //needs to be changed

    public static final int SHOOTER_SENSOR_ID_1 = 2; //needs to be changed
    public static final int SHOOTER_SENSOR_ID_2 = 1; //needs to be changed

    public static final double SHOOTER_MOTOR_AMP_SPEED = 1250; //multiplied power by 5000, need to refine later

    public static final double SHOOTER_MOTOR_LOW_SPEED = 3000; //multiplied power by 5000, need to refine later
    public static final double SHOOTER_MOTOR_HIGH_SPEED = 5000; //multiplied power by 5000, need to refine later

    public static final double SHOOTER_MOTOR_1_RPM = 12000;
    public static final double SHOOTER_MOTOR_2_RPM = 12000;
    public static final double SHOOTER_TIME_AFTER_TRIGGER = 3;

    //Shooter motor PID constants
    public static final double SHOOTER_MAX_RPM_ACCELERATION = 25000;
    public static final double SHOOTER_MAX_RPM_VELOCITY = 30000;

    public static final boolean FIELD_RELATIVE = true;

    //S = STEER, D = DRIVE, Drivetrain Constants
    public static final SwerveModuleProfile SWERVE_MODULE_PROFILE = SwerveModuleProfile.MK4;
    public static final int DRIVE_FRONT_RIGHT_S = 29;
    public static final int DRIVE_FRONT_RIGHT_D = 59;
    public static final int DRIVE_BACK_RIGHT_S = 30;
    public static final int DRIVE_BACK_RIGHT_D = 60;
    public static final int DRIVE_FRONT_LEFT_S = 28;
    public static final int DRIVE_FRONT_LEFT_D = 58;
    public static final int DRIVE_BACK_LEFT_S = 27;
    public static final int DRIVE_BACK_LEFT_D = 57;

    public static final int DRIVE_CANCODER_FRONT_RIGHT = 39;
    public static final int DRIVE_CANCODER_BACK_RIGHT = 40;
    public static final int DRIVE_CANCODER_FRONT_LEFT = 38;
    public static final int DRIVE_CANCODER_BACK_LEFT = 37;


    //PID Constants
    public static final double DRIVE_PID_P = 1;
    public static final double DRIVE_PID_I = 0;
    public static final double DRIVE_PID_D = 0;
    public static final double DRIVE_PID_FF_S = 1;
    public static final double DRIVE_PID_FF_V = 2.8;

    public static final double STEER_PID_P = 0.3;
    public static final double STEER_PID_I = 0;
    public static final double STEER_PID_D = 0.005;
    public static final double STEER_PID_FF_S = 0;//0.2;
    public static final double STEER_PID_FF_V = 0;//0.8;
    public static final double WHEEL_RADIUS = 0.0508;
    public static final double ROBOT_RADIUS =  0.381;

    public static final double MAX_VELOCITY = 4.8; // 4 meters per second
    // theoretical maximum angular velocity (radians/second)
    public static final double MAX_ANGULAR_SPEED = MAX_VELOCITY / (2 * ROBOT_RADIUS);
    public static final double ROBOT_WIDTH = 0.8636;
    public static final double ROBOT_LENGTH = 0.8636;
    public static final double AUTO_ALIGN_THRESHOLD = 2; //degrees

    public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 261.56;
    public static final double FRONT_LEFT_ABS_ENCODER_ZERO = 190.28;
    public static final double BACK_LEFT_ABS_ENCODER_ZERO = 119.35;
    public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = 306.29;

    //Feeder
    public static final double FEEDER_MOTOR_ENTER_SPEED = 0.5; //0.7
    public static final double FEEDER_BACK_DRIVE_SPEED = -0.2;
    public static final double FEEDER_MOTOR_EXIT_SPEED = 1;
    public static final int FEEDER_MOTOR_ID = 4;
    public static final int FEEDER_SENSOR_ID = 30; // this should be changed to the color sensor

    public static final double ARM_SEPERATION_DISTANCE = 5.00;
    public static final double OUTTAKE_SPEED = 1.00;
    public static final double CLIMBER_SPEED = 0.10;
    public static final int OUTTAKE_MOTOR1_ID = 6;
    public static final int OUTTAKE_MOTOR2_ID = 7;
    public static final int CLIMBER_LEFT = 52;
    public static final int CLIMBER_RIGHT = 53;
    public static final double CLIMBER_BALANCE_kP = 1;
    public static final double CLIMBER_BALANCE_kTi = 1;
    public static final double CLIMBER_Balance_KTd = 1;
    public static final double CLIMBER_BALANCE_LOW_SPEED=0.10;
    public static final double CLIMBER_BALANCE_HIGH_SPEED=0.50;
    public static final double CLIMBER_RAISING_SPEED = 0.2;
    public static final double CLIMBER_BALANCE_THRESH=30;
    public static final double CLIMBER_TIMEOUT_S=10;
    public static final double RAISING_TIMEOUT = 3;

    //Intake
    public static final int INTAKE_MOTOR_1_ID = 6;
    public static final int INTAKE_MOTOR_2_ID = 7;

    public static final double INTAKE_MOTOR_1_SPEED = 1;
    public static final double INTAKE_MOTOR_2_SPEED = 1;

    public static final int INTAKE_MOTOR_PEAK_CURRENT_LIMIT = 0; //value has to be tweaked
    public static final int INTAKE_MOTOR_PEAK_CURRENT_DURATION = 10; //value has to be tweaked
    public static final int INTAKE_MOTOR_CONTINUOUS_CURRENT_LIMIT = 1; //value has to be tweaked
    public static final boolean INTAKE_CURRENT_LIMIT_ENABLED = false; //value has to be tweaked

    //Deployer
    public static final int DEPLOYER_MOTOR_ID = 10;

    public static final double ALIGNABLE_PID_P = 0.015;
    public static final double ALIGNABLE_PID_I = 0;
    public static final double ALIGNABLE_PID_D = 0.0015;
    public static final double MAX_AUTO_ALIGN_SPEED = 0.9;
}
