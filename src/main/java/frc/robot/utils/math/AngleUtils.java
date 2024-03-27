package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Rotation2d;

public class AngleUtils {
    public static Rotation2d compliment(Rotation2d angle){
        return new Rotation2d(Math.PI/2).minus(angle);
    }
    public static Rotation2d reduce(Rotation2d angle){
        return new Rotation2d((angle.getRadians() % (2 * Math.PI)));
    }
    public static Rotation2d normalize(Rotation2d angle){
        return new Rotation2d(Math.asin(Math.sin(angle.getRadians())));
    }
}
