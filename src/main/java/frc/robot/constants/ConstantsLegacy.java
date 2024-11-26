package frc.robot.constants;

import edu.wpi.first.math.util.Units;
import frc.robot.swervev2.SwerveModuleProfile;

public class ConstantsLegacy extends GameConstants{
    //TODO: change all this probably
    //AMP 
    public static final int AMP_ID = 5;

    //RAMP
    public static final double RAMP_ERROR_RANGE = 0.00;
    public static final int RAMP_ID = 47;

    //Servo
    public static final int LEFT_SERVO_ID = 9;
    public static final int RIGHT_SERVO_ID = 8;
    public static final int LED_PWM_ID = 7;

    //Shooter
    public static final int SHOOTER_MOTOR_LEFT = 55; //needs to be changed
    public static final int SHOOTER_MOTOR_RIGHT = 56; //needs to be changed

    public static final int SHOOTER_SENSOR_ID_1 = 2; //needs to be changed
    public static final int SHOOTER_SENSOR_ID_2 = 1; //needs to be changed

    public static final double SHOOTER_MOTOR_1_RPM = 12000;
    public static final double SHOOTER_MOTOR_2_RPM = 12000;

    //Shooter motor PID constants
    public static final double SHOOTER_MAX_RPM_ACCELERATION = 25000;
    public static final double SHOOTER_MAX_RPM_VELOCITY = 30000;

    //S = STEER, D = DRIVE, Drivetrain Constants
    public static final SwerveModuleProfile SWERVE_MODULE_PROFILE = SwerveModuleProfile.ANYMARK_LEGACY_1;
    public static final int DRIVE_FRONT_RIGHT_S = 10;
    public static final int DRIVE_FRONT_RIGHT_D = 41;
    public static final int DRIVE_BACK_RIGHT_S = 11;
    public static final int DRIVE_BACK_RIGHT_D = 39;
    public static final int DRIVE_FRONT_LEFT_S = 3;
    public static final int DRIVE_FRONT_LEFT_D = 37;
    public static final int DRIVE_BACK_LEFT_S = 1;
    public static final int DRIVE_BACK_LEFT_D = 38;
    //TODO: make news constants file
    public static final int DRIVE_ANALOG_ENCODER_FRONT_RIGHT = 2; //TODO: CHANGE ALL LATER
    public static final int DRIVE_ANALOG_ENCODER_BACK_RIGHT = 3;
    public static final int DRIVE_ANALOG_ENCODER_FRONT_LEFT = 1;
    public static final int DRIVE_ANALOG_ENCODER_BACK_LEFT = 0;


    // public static final int DRIVE_CANCODER_FRONT_RIGHT = 39;
    // public static final int DRIVE_CANCODER_BACK_RIGHT = 40;
    // public static final int DRIVE_CANCODER_FRONT_LEFT = 38;
    // public static final int DRIVE_CANCODER_BACK_LEFT = 37;


    //PID Constants
    public static final double DRIVE_PID_P = 1;
    public static final double DRIVE_PID_I = 0;
    public static final double DRIVE_PID_D = 0;
    public static final double DRIVE_PID_FF_S = 1;
    public static final double DRIVE_PID_FF_V = 2.8;


    //Current Caps
    public static final int DRIVE_SMART_LIMIT = 38;
    public static final int DRIVE_SECONDARY_LIMIT = 48;
    public static final double DRIVE_RAMP_RATE_LIMIT = 0.1;

    public static final double STEER_PID_P = 0.3;
    public static final double STEER_PID_I = 0;
    public static final double STEER_PID_D = 0.005;
    public static final double STEER_PID_FF_S = 0;//0.2;
    public static final double STEER_PID_FF_V = 0;//0.8;
    public static final double WHEEL_RADIUS = 0.0508;
    public static final double ROBOT_RADIUS =  0.43;

    public static final double MAX_VELOCITY = 4.8; // 4 meters per second
    // theoretical maximum angular velocity (radians/second)
    public static final double MAX_ANGULAR_SPEED = Math.PI * 6;
    public static final double ROBOT_WIDTH = 0.8636;
    public static final double ROBOT_LENGTH = 0.8636;
    public static final double AUTO_ALIGN_THRESHOLD = 2.3; //degrees

    public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 261.56;
    public static final double FRONT_LEFT_ABS_ENCODER_ZERO = 190.28;
    public static final double BACK_LEFT_ABS_ENCODER_ZERO = 78.22;
    public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = 306.29;

    //Limelight
    public static final double MOVE_TO_GAMEPIECE_TURNING_P = 0.02;
    public static final double MOVE_TO_GAMEPIECE_TURNING_D = 0.00015;
    public static final double MOVE_TO_GAMEPIECE_MOVING_P = 0.08;

    //Vision
    public static final double  CAMERA_OFFSET_FROM_CENTER_X = Units.inchesToMeters(-5); //center of bot is 5" back from camera
    public static final double  CAMERA_OFFSET_FROM_CENTER_Y = Units.inchesToMeters(0);

    //Feeder
    public static final int FEEDER_MOTOR_ID = 4;
    public static final int FEEDER_SENSOR_ID = 30; // this should be changed to the color sensor

    public static final double ARM_SEPERATION_DISTANCE = 5.00;
    public static final double OUTTAKE_SPEED = 1.00;
    public static final double CLIMBER_SPEED = 0.10;
    public static final int OUTTAKE_MOTOR1_ID = 6;
    public static final int OUTTAKE_MOTOR2_ID = 7;
    public static final int CLIMBER_LEFT = 52;
    public static final int CLIMBER_RIGHT = 53;

    //Intake
    public static final int INTAKE_MOTOR_1_ID = 6;
    public static final int INTAKE_MOTOR_2_ID = 7;

    //Deployer
    public static final int DEPLOYER_MOTOR_ID = 10;

    public static final double ALIGNABLE_PID_P = 0.01;
    public static final double ALIGNABLE_PID_I = 0.003;
    public static final double ALIGNABLE_PID_D = 0.0004;
}