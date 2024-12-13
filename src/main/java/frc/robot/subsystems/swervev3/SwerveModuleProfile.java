package frc.robot.subsystems.swervev3;

import frc.robot.subsystems.swervev3.io.ModulePosition;

public enum SwerveModuleProfile {
    MK4(true, false, true, false,
            12.8, 6.75, false),
    MK4I(true, false, true, false,
            150f / 7f, 8.142857, true); //old

    SwerveModuleProfile(boolean frontRightInverted, boolean frontLeftInverted, boolean backRightInverted, boolean backLeftInverted, double steerRatio, double driveRatio, boolean steerInverted) {
        this.frontRightInverted = frontRightInverted;
        this.frontLeftInverted = frontLeftInverted;
        this.backRightInverted = backRightInverted;
        this.backLeftInverted = backLeftInverted;
        this.steerRatio = steerRatio;
        this.driveRatio = driveRatio;
        this.steerInverted = steerInverted;
    }

    private final boolean frontRightInverted;
    private final boolean frontLeftInverted;
    private final boolean backRightInverted;
    private final boolean backLeftInverted;
    private final double steerRatio;
    private final double driveRatio;
    private final boolean steerInverted;

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

    public double getSteerGearRatio() {
        return steerRatio;
    }

    public double getDriveGearRatio() {
        return driveRatio;
    }

    public boolean isSteerInverted() {
        return steerInverted;
    }
    public boolean isDriveInverted(ModulePosition modulePosition){
        switch (modulePosition){
            case FRONT_LEFT -> {
                return isFrontLeftInverted();
            }
            case FRONT_RIGHT -> {
                return isFrontRightInverted();
            }
            case BACK_LEFT -> {
                return isBackLeftInverted();
            }
            case BACK_RIGHT -> {
                return isBackRightInverted();
            }
            default -> {
                throw new IllegalArgumentException("Invalid Module Position" + modulePosition);
            }
        }
    }
}
