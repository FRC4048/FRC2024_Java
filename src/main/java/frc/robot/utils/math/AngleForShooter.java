package frc.robot.utils.math;

public class AngleForShooter {
    private final double motorMoveSmallWheel;
    private final double smallWheelMoveLargeWheel;
    private final double angleLargeWheelTick;
    private double motorSpins;

    public AngleForShooter(double motorMoveSmallWheel, double smallWheelMoveLargeWheel, double angleLargeWheelTick, double motorSpins) {
        this.motorMoveSmallWheel = motorMoveSmallWheel;
        this.smallWheelMoveLargeWheel = smallWheelMoveLargeWheel;
        this.angleLargeWheelTick = angleLargeWheelTick;
        this.motorSpins = motorSpins;
    }

    public double getAngleFromMotorSpins(double motorMoveSmallWheel, double smallWheelMoveLargeWheel, double angleLargeWheelTick) {
        return motorSpins * (angleLargeWheelTick / (smallWheelMoveLargeWheel * motorMoveSmallWheel));
    }
    
}
