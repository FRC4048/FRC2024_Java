package frc.robot;

public enum SwerveMotor {
    
    

    MK4(true, false, true, false, 
    12.8, 8.142857),
    MK4I(false, true, false, true, 
    150f/7f, 8.142857); //old

    SwerveMotor(boolean frontRightInverted, boolean frontLeftInverted, boolean backRightInverted, boolean backLeftInverted, double steerRatio, double driveRatio) {
        this.frontRightInverted = frontRightInverted;
        this.frontLeftInverted = frontLeftInverted;
        this.backRightInverted = backRightInverted;
        this.backLeftInverted = backLeftInverted;
        this.steerRatio = steerRatio;
        this.driveRatio = driveRatio;

    }

    private final boolean frontRightInverted;
    private final boolean frontLeftInverted;
    private final boolean backRightInverted;
    private final boolean backLeftInverted;
    private final double steerRatio;
    private final double driveRatio;


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




    
}
