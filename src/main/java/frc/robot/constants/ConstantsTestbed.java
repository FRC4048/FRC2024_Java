package frc.robot.constants;

import edu.wpi.first.math.util.Units;
import frc.robot.swervev2.SwerveModuleProfile;

public class ConstantsTestbed extends GameConstants {

    //AMP

    public static final int AMP_ID = 5;
    
    //RAMP
    public static final double RAMP_ERROR_RANGE = 0.00;
    public static final int RAMP_ID = 130;
    
    //Servo
    public static final int LEFT_SERVO_ID = 19;
    public static final int RIGHT_SERVO_ID = 18;
    public static final int LED_PWM_ID = 7;

    //Shooter
    public static final int SHOOTER_MOTOR_LEFT = 44;
    public static final int SHOOTER_MOTOR_RIGHT = 45;

    public static final int SHOOTER_SENSOR_ID_1 = 3;
    public static final int SHOOTER_SENSOR_ID_2 = 4;

    public static final double SHOOTER_MOTOR_AMP_SPEED = 1250; //multiplied power by 5000, need to refine later

    public static final double SHOOTER_MOTOR_LOW_SPEED = 3000; //multiplied power by 5000, need to refine later
    public static final double SHOOTER_MOTOR_HIGH_SPEED = 5000; //multiplied power by 5000, need to refine later


    //Shooter motor PID constants
    public static final double SHOOTER_MAX_RPM_ACCELERATION = 25000;
    public static final double SHOOTER_MAX_RPM_VELOCITY = 30000;

    //S = STEER, D = DRIVE, Drivetrain Constants
    public static final SwerveModuleProfile SWERVE_MODULE_PROFILE = SwerveModuleProfile.MK4;
    public static final int DRIVE_FRONT_RIGHT_S = 430;
    public static final int DRIVE_FRONT_RIGHT_D = 440;
    public static final int DRIVE_BACK_RIGHT_S = 450;
    public static final int DRIVE_BACK_RIGHT_D = 460;
    public static final int DRIVE_FRONT_LEFT_S = 470;
    public static final int DRIVE_FRONT_LEFT_D = 480;
    public static final int DRIVE_BACK_LEFT_S = 490;
    public static final int DRIVE_BACK_LEFT_D = 500;

    public static final int DRIVE_CANCODER_FRONT_RIGHT = 520;
    public static final int DRIVE_CANCODER_BACK_RIGHT = 530;
    public static final int DRIVE_CANCODER_FRONT_LEFT = 540;
    public static final int DRIVE_CANCODER_BACK_LEFT = 550;


    //PID Constants
    public static final double DRIVE_PID_P = 1;
    public static final double DRIVE_PID_I = 0;
    public static final double DRIVE_PID_D = 0;
    public static final double DRIVE_PID_FF_S = 1;
    public static final double DRIVE_PID_FF_V = 2.8;
    //Current Caps
    public static final int DRIVE_SMART_LIMIT = 30;
    public static final int DRIVE_SECONDARY_LIMIT = 40;
    public static final double DRIVE_RAMP_RATE_LIMIT = 0.25;

    public static final double STEER_PID_P = 0.3;
    public static final double STEER_PID_I = 0;
    public static final double STEER_PID_D = 0.005;
    public static final double STEER_PID_FF_S = 0;//0.2;
    public static final double STEER_PID_FF_V = 0;//0.8;
    public static final double WHEEL_RADIUS = 0.0508;
    public static final double ROBOT_RADIUS =  0.381;

    public static final double MAX_VELOCITY = 4.8; // 4 meters per second
    public static final double MAX_ANGULAR_SPEED = Math.PI * 6; // 1 rotation per second
    public static final double ROBOT_WIDTH = 0.8636;
    public static final double ROBOT_LENGTH = 0.8636;
    public static final double AUTO_ALIGN_THRESHOLD = 2; //degrees

    public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 190.28;
    public static final double FRONT_LEFT_ABS_ENCODER_ZERO = 261.56;
    public static final double BACK_LEFT_ABS_ENCODER_ZERO = 306.29;
    public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = 119.35;

    //Limelight
    public static final double MOVE_TO_GAMEPIECE_TURNING_P = 0.02;
    public static final double MOVE_TO_GAMEPIECE_TURNING_D = 0.00015;
    public static final double MOVE_TO_GAMEPIECE_MOVING_P = 0.06;

    //Vision
    public static final double  CAMERA_OFFSET_FROM_CENTER_X = Units.inchesToMeters(0);
    public static final double  CAMERA_OFFSET_FROM_CENTER_Y = Units.inchesToMeters(0);

    //Feeder
    public static final int FEEDER_MOTOR_ID = 111;
    public static final int FEEDER_SENSOR_ID = 30;

    public static final double ARM_SEPERATION_DISTANCE = 5.00;
    public static final int OUTTAKE_MOTOR1_ID = 880;
    public static final int OUTTAKE_MOTOR2_ID = 890;
    public static final int CLIMBER_LEFT = 144;
    public static final int CLIMBER_RIGHT = 145;

    //Intake
    public static final int INTAKE_MOTOR_1_ID = 211;
    public static final int INTAKE_MOTOR_2_ID = 12;


    //Deployer
    public static final double ALIGNABLE_PID_P = 0.015;
    public static final double ALIGNABLE_PID_I = 0;
    public static final double ALIGNABLE_PID_D = 0.0015;
    public static final int DEPLOYER_MOTOR_ID = 11;
}
