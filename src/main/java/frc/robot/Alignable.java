package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;

import java.util.function.BiFunction;

public enum Alignable {
    AMP(0,0,(x,y)-> new Rotation2d(Math.PI / 2)),
    SPEAKER(0.46,5.67,(x2,y2)-> new Rotation2d(RobotContainer.shouldFlip() ? 0 : Math.PI).plus(new Rotation2d(Math.atan(y2/x2))));

    private final double blueX;
    private final double y;
    private final BiFunction<Double, Double, Rotation2d> functionFromDist;

    Alignable(double blueX, double y, BiFunction<Double,Double,Rotation2d> functionFromDist) {
        this.blueX = blueX;
        this.y = y;
        this.functionFromDist = functionFromDist;
    }

    public double getX() {
        return RobotContainer.shouldFlip() ? blueX + 16 : blueX;
    }

    public double getY() {
        return y;
    }

    public Rotation2d getAngle(double x, double y) {
        return functionFromDist.apply(x - getX(), y - getY());
    }
    public double calcTurnSpeed(Rotation2d currentAngle, double x, double y){
        double diff = currentAngle.minus(getAngle(x,y)).getDegrees();
        if (Math.abs(diff) < 2) return 0;
        return (Math.abs(diff)) / (360) * Math.signum(diff);

    }
}
