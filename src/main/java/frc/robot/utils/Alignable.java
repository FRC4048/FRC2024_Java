package frc.robot.utils;

import frc.robot.RobotContainer;

public enum Alignable {
    AMP(0,0,0),
    SPEAKER(0.46,16.46,5.67);

    private final double blueX;
    private final double redX;
    private final double y;

    Alignable(double blueX, double redX, double y) {
        this.blueX = blueX;
        this.redX = redX;
        this.y = y;
    }

    public double getX() {
        return RobotContainer.isRedAlliance() ? redX : blueX;
    }

    public double getY() {
        return y;
    }

}
