package frc.robot.utils;

import frc.robot.RobotContainer;

public enum Alignable {
    AMP(0,0,0,0),
    SPEAKER(0.46,16.46,5.548,2);

    private final double blueX;
    private final double redX;
    private final double y;
    private final double z;

    Alignable(double blueX, double redX, double y, double z) {
        this.blueX = blueX;
        this.redX = redX;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return RobotContainer.isRedAlliance() ? redX : blueX;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
