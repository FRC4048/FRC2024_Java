package frc.robot.utils;

import frc.robot.RobotContainer;

public enum Alignable {
    AMP(0,0,0,0),
    SPEAKER(-0.0381+0.234809,16.58-0.234809,5.548,1.95);//1.343 - dist center of robot to speaker shot //value v2 1.343 - 0.447649 - Constants.ROBOT_LENGTH / 2

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
