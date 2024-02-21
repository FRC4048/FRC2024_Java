package frc.robot.constants;

public class GameConstants {
    public static final boolean ENABLE_LOGGING = false;

    //JOYSTICKS
    public static final int LEFT_JOYSICK_ID = 0;
    public static final int RIGHT_JOYSTICK_ID = 1;
    public static final int XBOX_CONTROLLER_ID = 3;
    public static final double DIAG_REL_SPARK_ENCODER = 0.1;
    public static final int DIAG_ABS_SPARK_ENCODER = 20;
    public static final double GRAVITY = -9.81;

    //DEBUGS
    public static final boolean SHOOTER_DEBUG = false;
    public static final boolean RAMP_DEBUG = true;
    public static final boolean SWERVE_DEBUG = false;
    public static final boolean FEEDER_DEBUG = false;
    public static final boolean CLIMBER_DEBUG = false;
    public static final boolean INTAKE_DEBUG = false;
    public static final boolean DEPLOYER_DEBUG = false;

    //DEPLOYER
    public static final int DEPLOYER_LOWER_TIMEOUT = 5;
    public static final double DEPLOYER_RAISE_TIMEOUT = 5;
    public static final double DEPLOYER_LOWER_SPEED = 0.9;
    public static final double DEPLOYER_RAISE_SPEED = -0.9;
    public static final double SHOOT_AMP_MOTOR_SPEED = 0.4;//0.4
    public static final double SHOOTER_VELOCITY = 6.096;//m/s @ 90%,50%

    public static final double MAX_RAMP_ENC = 25.0000;

    public static final double DRIVE_THRESHHOLD_METERS = 0.00762;// TODO: Refine This Number

}
