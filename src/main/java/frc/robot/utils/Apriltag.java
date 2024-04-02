package frc.robot.utils;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;

public enum Apriltag {
    ONE(Units.inchesToMeters(593.68), Units.inchesToMeters(9.68), Units.inchesToMeters(53.38)),
    TWO(Units.inchesToMeters(637.21), Units.inchesToMeters(34.79), Units.inchesToMeters(53.38)),
    THREE(Units.inchesToMeters(652.73), Units.inchesToMeters(196.17), Units.inchesToMeters(57.13)),
    FOUR(Units.inchesToMeters(652.73), Units.inchesToMeters(218.42), Units.inchesToMeters(57.13)),
    FIVE(Units.inchesToMeters(578.77), Units.inchesToMeters(323.00), Units.inchesToMeters(53.38)),
    SIX(Units.inchesToMeters(72.5), Units.inchesToMeters(323.00), Units.inchesToMeters(53.38)),
    SEVEN(Units.inchesToMeters(-1.50), Units.inchesToMeters(218.42), Units.inchesToMeters(57.13)),
    EIGHT(Units.inchesToMeters(-1.50), Units.inchesToMeters(196.17), Units.inchesToMeters(57.13)),
    NINE(Units.inchesToMeters(14.02), Units.inchesToMeters(34.79), Units.inchesToMeters(53.38)),
    TEN(Units.inchesToMeters(57.54), Units.inchesToMeters(9.68), Units.inchesToMeters(53.38)),
    ELEVEN(Units.inchesToMeters(468.69), Units.inchesToMeters(146.19), Units.inchesToMeters(52.00)),
    TWELVE(Units.inchesToMeters(468.69), Units.inchesToMeters(177.10), Units.inchesToMeters(52.00)),
    THIRTEEN(Units.inchesToMeters(441.74), Units.inchesToMeters(161.62), Units.inchesToMeters(52.00)),
    FOURTEEN(Units.inchesToMeters(209.48), Units.inchesToMeters(161.62), Units.inchesToMeters(52.00)),
    FIFTEEN(Units.inchesToMeters(182.73), Units.inchesToMeters(177.10), Units.inchesToMeters(52.00)),
    SIXTEEN(Units.inchesToMeters(182.73), Units.inchesToMeters(146.19), Units.inchesToMeters(52.00));

    private final double x;
    private final double y;
    private final double z;

    Apriltag(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
    public Translation3d getPose(){
        return new Translation3d(x,y,z);
    }
    public static Apriltag of(int num){
        return values()[num];
    }
}
