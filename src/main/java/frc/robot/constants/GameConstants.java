package frc.robot.constants;

public class GameConstants {
    //JOYSTICKS
    public static final int LEFT_JOYSICK_ID = 0;
    public static final int RIGHT_JOYSTICK_ID = 1;
    public static final int XBOX_CONTROLLER_ID = 2;

    public static final double AUTO_SPOOL_AND_SHOOT_TIME = 4;
    //DEBUGS
    public static final boolean SHOOTER_DEBUG = false;
    public static final boolean RAMP_DEBUG = false;
    public static final boolean RAMP_PID_DEBUG = false;
    public static final boolean SWERVE_DEBUG = false;
    public static final boolean FEEDER_DEBUG = false;
    public static final boolean CLIMBER_DEBUG = true;
    public static final boolean INTAKE_DEBUG = false;
    public static final boolean DEPLOYER_DEBUG = true;
    public static final boolean AMP_DEBUG = false;
    public static final boolean VISION_DEBUG = false;
    public static final boolean PATHPLANNER_DEBUG = false;
    public static final boolean ENABLE_LOGGING = true;
    public static final boolean ENABLE_VISION = true;

    //AMP
    public static final double SHOOT_AMP_MOTOR_SPEED = 0.2;//0.2
    public static final double AMP_MOTOR_SPEED = .8;
    public static final double AMP_TIMEOUT = 2.0;

    //DEPLOYER
    public static final int DEPLOYER_LOWER_TIMEOUT = 1;
    public static final double DEPLOYER_RAISE_TIMEOUT = 1;
    public static final double DEPLOYER_LOWER_SPEED = 0.9;
    public static final double DEPLOYER_RAISE_SPEED = -0.9;
    public static final double SPOOL_TIME = 0.5;//seconds

    //RAMP
    public static final double RAMP_POS = 0.0;
    public static final double AMP_RAMP_ENC_VALUE = 5.75;
    public static final double MAX_RAMP_ENC = 25.0000;
    public static final double RESET_RAMP_SPEED = -0.3;
    public static final double RAMP_ANGLE = 15.0;
    public static final double RAMP_MAX_RPM_ACCELERATION = 3000;
    public static final double RESET_RAMP_TIMEOUT = 1.5;
    public static final double RAMP_POS_THRESHOLD = 0.1;

    public static final double RAMP_POS_TIMEOUT = 3;
    public static final double RAMP_POS_SAFE_AMP_DEPLOY = 15.0;
    public static final double RAMP_POS_STOW = 0.5;
    public static final double RAMP_POS_SHOOT_SPEAKER_CLOSE = 0.1;
    public static final double RAMP_POS_SHOOT_SPEAKER_AWAY = 7.8; //when about 44" away from the speaker
    public static final double RAMP_POS_SHOOT_AMP = 7.0;

    //SERVO
    public static final int RIGHT_SERVO_ENGAGED = 0;
    public static final int RIGHT_SERVO_DISENGAGED = 180;
    public static final int LEFT_SERVO_ENGAGED = 0;
    public static final int LEFT_SERVO_DISENGAGED = 180;

    //SHOOTER
    public static final double SHOOTER_MOTOR_SPEED_TRESHOLD = 100; //TODO: Refine This Number
    public static final double SHOOTER_MOTOR_LOW_SPEED = 3500; //multiplied power by 5000, need to refine later
    public static final double SHOOTER_MOTOR_HIGH_SPEED = 5500; //multiplied power by 5000, need to refine later
    public static final double SHOOTER_MOTOR_AMP_SPEED = 1100; //multiplied power by 5000, need to refine later
    public static final double SHOOTER_MOTOR_1_RPM = 12000;
    public static final double SHOOTER_MOTOR_2_RPM = 12000;
    public static final double SHOOTER_TIME_AFTER_TRIGGER = 3;
    public static final double SHOOTER_VELOCITY = 8.9;//m/s @ 5500rpm, 3500rpm
    public static final double ADVANCED_SPINNING_SHOT_TIMEOUT = 15;
    public static final double SHOOTER_PID_P = 0.000058;
    public static final double SHOOTER_PID_I = 0.0;
    public static final double SHOOTER_PID_D = 0.00001;
    public static final double SHOOTER_PID_FF = 0.00017;
    public static final double SHOOTER_TIME_BEFORE_STOPPING = 0.5;

    //FEEDER
    public static final double FEEDER_MOTOR_ENTER_SPEED = 0.5;
    public static final double FEEDER_BACK_DRIVE_SPEED = -0.25;
    public static final double FEEDER_MOTOR_SPEAKER_SPEED = 1.0;
    public static final double FEEDER_MOTOR_AMP_SPEED = 0.7;
    public static final double FEEDER_BACK_DRIVE_TIMEOUT = 10.0;
    public static final double FEEDER_GAMEPIECE_UNTIL_LEAVE_TIMEOUT = 5.0;
    public static final double START_FEEDER_TIMEOUT = 5.0;
    public static final double FEEDER_PIECE_NOT_SEEN_COUNTER = 30;
    public static final double FEEDER_BACK_DRIVE_DELAY = 0.4;
    public static final double FEEDER_WAIT_TIME_BEFORE_BACKDRIVE = 0.5;
    public static final double COLOR_CONFIDENCE_RATE_INCOMING = .85;
    public static final double COLOR_CONFIDENCE_RATE_BACKDRIVE = .92;

    //CLIMBER
    public static final double OUTTAKE_SPEED = 1.00;
    public static final double CLIMBER_SPEED = 0.10;
    public static final double CLIMBER_BALANCE_LOW_SPEED = 0.10;
    public static final double CLIMBER_BALANCE_HIGH_SPEED = 0.50;
    public static final double CLIMBER_BALANCE_THRESH = 30;
    public static final double CLIMBER_TIMEOUT_S = 10;
    public static final double CLIMBER_RAISING_SPEED = 0.2;
    public static final double MAX_CLIMBER_ENCODER = 80.0;
    public static final double RAISING_TIMEOUT = 3;
    public static final double RESET_CLIMBER_TIMEOUT = 1;
    public static final double CLIMBER_TORTURE_SPEED = 1.0;

    //INTAKE
    public static final double INTAKE_MOTOR_1_SPEED = 0.8;
    public static final double INTAKE_MOTOR_2_SPEED = 0.8;
    public static final int INTAKE_MOTOR_PEAK_CURRENT_LIMIT = 0; //value has to be tweaked
    public static final int INTAKE_MOTOR_PEAK_CURRENT_DURATION = 10; //value has to be tweaked
    public static final int INTAKE_MOTOR_CONTINUOUS_CURRENT_LIMIT = 1; //value has to be tweaked
    public static final boolean INTAKE_CURRENT_LIMIT_ENABLED = false; //value has to be tweaked

    //DRIVETRAIN
    public static final double DRIVE_THRESHHOLD_METERS = 0.00762;// TODO: Refine This Number
    public static final double MOVE_DISTANCE_TIMEOUT = 5.0;
    public static final double MAX_AUTO_ALIGN_SPEED = 0.9;
    public static final double TURN_TO_GAMEPIECE_TURNING_P = 0.02;
    public static final double TURN_TO_GAMEPIECE_TURNING_D = 0.00015;
    public static final double TURN_TO_GAMEPIECE_MOVING_P = 0.06;

    //Limelight
    public static final double LIMELIGHT_TURN_TO_PIECE_DESIRED_Y = -21;
    public static final double LIMELIGHT_TURN_TO_PIECE_DESIRED_X = -8; //Put Gampeiece in middle to get offset
    public static final int INTAKE_LIME_TIMEOUT = 5;
    public static final double PIECE_LOST_TIME_THRESHOLD = 0.5;
    public static final double LIMELIGHT_PIECE_NOT_SEEN_COUNT = 30;
    public static final double TIMEOUT_AFTER_PIECE_NOT_SEEN = 0.5;
    public static final double GAMEPIECE_MAX_VELOCITY = 150 * 4.8 / (2* 0.381);
    public static final double GAMEPIECE_MAX_ACCELERATION = 2 * Math.PI * 150;
    public static final double MOVE_TO_GAMEPIECE_THRESHOLD = 1;
    public static final double MOVE_TO_GAMEPIECE_TIMEOUT = 5;
    public static final String LIMELIGHT_IP_ADDRESS = "10.40.48.36";

    //Miscellaneous
    public static final boolean FIELD_RELATIVE = true;
    public static final double PIECE_THRESHOLD = 0.7;
    public static final double GRAVITY = -9.81;
    public static final int DIAG_ABS_SPARK_ENCODER = 20;
    public static final double DIAG_REL_SPARK_ENCODER = 0.1;
    public static final double SPEAKER_TOP_EDGE_Y_POS = 6;
    public static final double PATH_PLANNER_TRANSLATION_PID_P = 5;
    public static final double PATH_PLANNER_TRANSLATION_PID_I = 0;
    public static final double PATH_PLANNER_TRANSLATION_PID_D = 0;
    public static final double PATH_PLANNER_ROTATION_PID_P = 4.75;
    public static final double PATH_PLANNER_ROTATION_PID_I = 0;
    public static final double PATH_PLANNER_ROTATION_PID_D = 0;
    public static final double RAMP_CENTER_AUTO_SHOOT = 6;
    public static final double RAMP_SIDE_AUTO_SHOOT = 5;
    public static final double RAMP_DIP_AUTO_SHOOT = 6;
    public static final int TIMED_INTAKE_AUTO_TIMEOUT = 2;

    public static final double HIGHT_OF_RAMP = 0.66; // needs to be measured
    public static final double RAMP_MIN_ANGLE = 31; //degrees
    public static final double RAMP_MAX_ANGLE = 52; //degrees

}
