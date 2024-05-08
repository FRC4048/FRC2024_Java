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
    public static final boolean SWERVE_DEBUG = false;
    public static final boolean FEEDER_DEBUG = false;
    public static final boolean CLIMBER_DEBUG = false;
    public static final boolean INTAKE_DEBUG = false;
    public static final boolean DEPLOYER_DEBUG = false;
    public static final boolean VISION_DEBUG = false;
    public static final boolean PATHPLANNER_DEBUG = false;
    public static final boolean ENABLE_LOGGING = true;
    public static final boolean ENABLE_VISION = true;
    public static final boolean LED_DEBUG = false;
    public static final boolean RELY_COLOR_SENSOR = false;

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
    public static final double RAMP_POS_SHOOT_SPEAKER_AWAY = 7.4; //when about 44" away from the speaker
    public static final double RAMP_POS_SHOOT_AMP = 7.0;
    public static final double RAMP_POS_SKIP = 18.0;

    //SERVO
    public static final int RIGHT_SERVO_ENGAGED = 180;
    public static final int RIGHT_SERVO_DISENGAGED = 0;
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
    public static final double SHOOTER_VELOCITY = 10.56;//m/s @ 5500rpm, 3500rpm // 8.9 or 8.1 or 10.65 or
    public static final double ADVANCED_SPINNING_SHOT_TIMEOUT = 15;
    public static final double SHOOTER_PID_P = 0.000058;
    public static final double SHOOTER_PID_I = 0.0;
    public static final double SHOOTER_PID_D = 0.00001;
    public static final double SHOOTER_PID_FF = 0.00017;
    public static final double SHOOTER_TIME_BEFORE_STOPPING = 0.5;
    public static final double SHOOTER_UP_TO_SPEED_THRESHOLD = 90;
    public static final double SHOOTER_MOTOR_STARTUP_OFFSET = 0.2; 

    //FEEDER
    public static final double FEEDER_MOTOR_ENTER_SPEED = 0.9;
    public static final double FEEDER_SLOW_SPEED = 0.3;
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

    //INTAKE
    public static final double INTAKE_MOTOR_1_SPEED = 0.9;
    public static final double INTAKE_MOTOR_2_SPEED = 0.9;
    public static final double INTAKE_SPOOL_TIME = 0.25;
    public static final int INTAKE_MOTOR_PEAK_CURRENT_LIMIT = 0; //value has to be tweaked
    public static final int INTAKE_MOTOR_PEAK_CURRENT_DURATION = 10; //value has to be tweaked
    public static final int INTAKE_MOTOR_CONTINUOUS_CURRENT_LIMIT = 1; //value has to be tweaked
    public static final boolean INTAKE_CURRENT_LIMIT_ENABLED = false; //value has to be tweaked
    public static final double INTAKE_PIECE_THESHOLD = 15;
    public static final int INTAKE_SPIKE_THESHOLD = 4;
    public static final double CURRENT_INTAKE_TIMEOUT = 10;

    //DRIVETRAIN
    public static final double DRIVE_THRESHHOLD_METERS = 0.00762;// TODO: Refine This Number
    public static final double MOVE_DISTANCE_TIMEOUT = 5.0;
    public static final double MAX_AUTO_ALIGN_SPEED = 0.9;

    //Limelight
    public static final double LIMELIGHT_MOVE_TO_PIECE_DESIRED_Y = -17;
    public static final double LIMELIGHT_MOVE_TO_PIECE_DESIRED_X = -10; //Put Gampeiece in middle to get offset
    public static final double LIMELIGHT_PIECE_NOT_SEEN_COUNT = 10;
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
    public static final double RAMP_ENCODER_TO_ANGLE_SLOPE = 2.5;
    public static final double RAMP_ENCODER_TO_ANGLE_Y_INTERCEPT = 30.5;
    public static final double TIMED_FEEDER_EXIT = 0.5;
    public static final double RAMP_PID_P = .0000055;
    public static final double RAMP_PID_FAR_FF = 0.00031;
    public static final double RAMP_ELIM_FF_THRESHOLD = 0.075;
    public static final int LIGHTSTRIP_PORT = 7;
    public static final double ROBOT_FROM_GROUND = 0.2032;
    public static final double RAMP_FROM_CENTER = 0.17;
    public static final double RAMP_AT_POS_THRESHOLD = 0.1;

    public static final long GYRO_THREAD_RATE_MS = 10;
    public static final long POSE_BUFFER_STORAGE_TIME = 2;
    public static final boolean MULTI_CAMERA = false;
    public static final double APRILTAG_SPEAKER_OFFSET = 0.234809;
    public static final double VISION_CONSISTANCY_THRESHOLD = 0.25;
    public static final boolean FILTER_VISION_POSES = true;
    public static final double VISION_CONSISTANCY_THRESHOLD = 0.25;

}
