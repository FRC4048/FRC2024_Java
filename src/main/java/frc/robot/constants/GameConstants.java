package frc.robot.constants;

public class GameConstants {
    public static final boolean ENABLE_LOGGING = true;

    //JOYSTICKS
    public static final int LEFT_JOYSICK_ID = 0;
    public static final int RIGHT_JOYSTICK_ID = 1;
    public static final int XBOX_CONTROLLER_ID = 2;
    public static final int DIAG_ABS_SPARK_ENCODER = 20;
    public static final double DIAG_REL_SPARK_ENCODER = 0.1;
    public static final double PIECE_THRESHOLD = 0.7;
    public static final double GRAVITY = -9.81;

    public static final double AUTO_SPOOL_AND_SHOOT_TIME = 4;
    //DEBUGS
    public static final boolean SHOOTER_DEBUG = false;
    public static final boolean RAMP_DEBUG = false;
    public static final boolean RAMP_PID_DEBUG = false;
    public static final boolean SWERVE_DEBUG = false;
    public static final boolean FEEDER_DEBUG = false;
    public static final boolean CLIMBER_DEBUG = false;
    public static final boolean INTAKE_DEBUG = false;
    public static final boolean DEPLOYER_DEBUG = false;
    public static final boolean AMP_DEBUG = false;
    public static final boolean PATHPLANNER_DEBUG = true;

    //DEPLOYER
    public static final int DEPLOYER_LOWER_TIMEOUT = 1;
    public static final double DEPLOYER_RAISE_TIMEOUT = 1;
    public static final double DEPLOYER_LOWER_SPEED = 0.9;
    public static final double DEPLOYER_RAISE_SPEED = -0.9;
    public static final double SHOOT_AMP_MOTOR_SPEED = 0.2;//0.2
    public static final double SPOOL_TIME = 0.5;//seconds
    public static final double FEEDER_BACK_DRIVE_DELAY = 0.4;
    public static final double AMP_RAMP_ENC_VALUE = 5.75;//
    public static final double SHOOTER_VELOCITY = 6.096;//m/s @ 90%,50%

    // FEEDER
    public static final double COLOR_CONFIDENCE_RATE_INCOMING = .85;
    public static final double COLOR_CONFIDENCE_RATE_BACKDRIVE = .92;

    public static final double MAX_RAMP_ENC = 25.0000;
    public static final double RAMP_POS_THRESHOLD = 0.1;

    public static final double RAMP_POS_TIMEOUT = 3;
    public static final double RAMP_POS_SHOOT_SPEAKER_CLOSE = 0.1;
    public static final double RAMP_POS_SHOOT_SPEAKER_AWAY = 7.0; //when about 44" away from the speaker
    public static final double RAMP_POS_SAFE_AMP_DEPLOY = 15.0;
    public static final double RAMP_POS_STOW = 0.5; // if this is 0, it tents to timeout
    public static final double RAMP_POS_SHOOT_AMP = 11.0;

    public static final double SHOOTER_MOTOR_SPEED_TRESHOLD = 100; //TODO: Refine This Number
    public static final double DRIVE_THRESHHOLD_METERS = 0.00762;// TODO: Refine This Number

    public static final double MAX_CLIMBER_ENCODER = 80.0;

    public static final double AMP_MOTOR_SPEED = .8;
    public static final double AMP_TIMEOUT = 2.0;

    // Shooter
    public static final double SHOOTER_PID_P = 0.000058;
    public static final double SHOOTER_PID_I = 0.0;
    public static final double SHOOTER_PID_D = 0.00001;
    public static final double SHOOTER_PID_FF = 0.00017;
    public static final double SHOOTER_TIME_BEFORE_STOPPING = 0.3;


    // Feeder
    public static final double FEEDER_MIN_TIME_FOR_SHOOTING = 3.0;
    public static final double FEEDER_WAIT_TIME_BEFORE_BACKDRIVE = 0.5;
    public static final int FEEDER_PIECE_NOT_SEEN_COUNTER = 30;
    public static final double FEEDER_BACK_DRIVE_TIMEOUT = 4.0;
    public static final double FEEDER_GAMEPIECE_UNTIL_LEAVE_TIMEOUT = 10.0;

    //OTHER TIMEOUTS

    public static final double MOVE_DISTANCE_TIMEOUT = 5.0;
    public static final double START_FEEDER_TIMEOUT = 5.0;
    public static final double RESET_RAMP_TIMEOUT = 5.0;
    public static final double SPEAKER_TOP_EDGE_Y_POS = 6;
    public static final double ADVANCED_SPINNING_SHOT_TIMEOUT = 15;
    public static final double PATH_PLANNER_TRANSLATION_PID_P = 5;
    public static final double PATH_PLANNER_TRANSLATION_PID_I = 0;
    public static final double PATH_PLANNER_TRANSLATION_PID_D = 0;
    public static final double PATH_PLANNER_ROTATION_PID_P = 4.75;
    public static final double PATH_PLANNER_ROTATION_PID_I = 0;
    public static final double PATH_PLANNER_ROTATION_PID_D = 0;

}
