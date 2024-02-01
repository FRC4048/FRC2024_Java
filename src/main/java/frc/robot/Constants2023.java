package frc.robot;

public class Constants2023 extends GameConstants{
    public static final double RAMP_PID_P = 1.00;
    public static final double RAMP_PID_I = 0.00;
    public static final double RAMP_PID_D = 0.00;
    public static final double RAMP_PID_FF = 1.00;
    public static final double RAMP_ERROR_IZONE = 0.00;
    public static final double RAMP_POS = 0.0;
    public static final int RAMP_ID = 45;
  
  
  
      //S = STEER, D = DRIVE, Drivetrain ConstantsS
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
      public static final double CHASSIS_DRIVE_GEAR_RATIO = 8.142857; // this value should be x:1
      public static final double CHASSIS_STEER_GEAR_RATIO = 150f/7f; // this value should be x:1
  
      public static final double MAX_VELOCITY = 4.0; // 4 meters per second
      public static final double MAX_ANGULAR_SPEED = Math.PI * 6; // 1 rotation per second
      public static final double ROBOT_WIDTH = 0.5969;
      public static final double ROBOT_LENGTH = 0.5969;
  
      public static final double BACK_RIGHT_ABS_ENCODER_ZERO = 54.35;
      public static final double FRONT_LEFT_ABS_ENCODER_ZERO = 339.5;
      public static final double BACK_LEFT_ABS_ENCODER_ZERO = 18.72;
      public static final double FRONT_RIGHT_ABS_ENCODER_ZERO = 133.3;
}
