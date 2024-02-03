package frc.robot.autochooser;

import frc.robot.Constants2023;
import frc.robot.Constants2024;

public enum SwerveMotor {
    
    

    MKI(false, false, false, false, 
    Constants2024.CHASSIS_STEER_GEAR_RATIO, Constants2024.CHASSIS_DRIVE_GEAR_RATIO, 
    Constants2024.FRONT_RIGHT_ABS_ENCODER_ZERO, Constants2024.FRONT_LEFT_ABS_ENCODER_ZERO, Constants2024.BACK_RIGHT_ABS_ENCODER_ZERO, Constants2024.BACK_LEFT_ABS_ENCODER_ZERO),
    MKI4(false, false, false, false, 
    Constants2023.CHASSIS_STEER_GEAR_RATIO, Constants2023.CHASSIS_DRIVE_GEAR_RATIO,
    Constants2023.FRONT_RIGHT_ABS_ENCODER_ZERO, Constants2023.FRONT_LEFT_ABS_ENCODER_ZERO, Constants2023.BACK_RIGHT_ABS_ENCODER_ZERO, Constants2023.BACK_LEFT_ABS_ENCODER_ZERO);

    private final boolean frontRightInverted;
    private final boolean frontLeftInverted;
    private final boolean backRightInverted;
    private final boolean backLeftInverted;
    private final double frontRightAbs;
    private final double frontLeftAbs;
    private final double backRightAbs;
    private final double backLeftAbs;
    private final double steerRatio;
    private final double driveRatio;
    

    public double getFrontRightAbs() {
        return frontRightAbs;
    }




    public double getFrontLeftAbs() {
        return frontLeftAbs;
    }




    public double getBackRightAbs() {
        return backRightAbs;
    }




    public double getBackLeftAbs() {
        return backLeftAbs;
    }

    


    public boolean isFrontRightInverted() {
        return frontRightInverted;
    }




    public boolean isFrontLeftInverted() {
        return frontLeftInverted;
    }




    public boolean isBackRightInverted() {
        return backRightInverted;
    }




    public boolean isBackLeftInverted() {
        return backLeftInverted;
    }




    public double getSteerRatio() {
        return steerRatio;
    }




    public double getDriveRatio() {
        return driveRatio;
    }




    SwerveMotor(boolean frontRightInverted, boolean frontLeftInverted, boolean backRightInverted, boolean backLeftInverted, double steerRatio, double driveRatio, double frontRightAbs, double frontLeftAbs, double backRightAbs, double backLeftAbs) {
        this.frontRightInverted = frontRightInverted;
        this.frontLeftInverted = frontLeftInverted;
        this.backRightInverted = backRightInverted;
        this.backLeftInverted = backLeftInverted;
        this.frontRightAbs = frontRightAbs;
        this.frontLeftAbs = frontLeftAbs;
        this.backRightAbs = backRightAbs;
        this.backLeftAbs = backLeftAbs;
        this.steerRatio = steerRatio;
        this.driveRatio = driveRatio;

    }
}
