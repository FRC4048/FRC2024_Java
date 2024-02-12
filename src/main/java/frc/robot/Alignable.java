package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;

import java.util.function.BiFunction;

public enum Alignable {
    AMP(0,0,0,(x,y)-> new Rotation2d(Math.PI / 2)),
    SPEAKER(0.46,16.46,5.67,(x2,y2)-> new Rotation2d(RobotContainer.isRedAlliance() ? 0 : Math.PI).plus(new Rotation2d(Math.atan(y2/x2))));

    private final double blueX;
    private final double redX;
    private final double y;
    private final BiFunction<Double, Double, Rotation2d> functionFromDist;

    Alignable(double blueX, double redX, double y, BiFunction<Double,Double,Rotation2d> functionFromDist) {
        this.blueX = blueX;
        this.redX = redX;
        this.y = y;
        this.functionFromDist = functionFromDist;
    }

    public double getX() {
        return RobotContainer.isRedAlliance() ? redX : blueX;
    }

    public double getY() {
        return y;
    }

    public Rotation2d getAngle(double x, double y) {
        return functionFromDist.apply(x - getX(), y - getY());
    }
}
