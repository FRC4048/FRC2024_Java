package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants;

public class VectorUtils {
    public static VelocityVector fromComponentVelocities(double xSpeed, double ySpeed, double angle){
        return new VelocityVector(combineComponents(xSpeed, ySpeed),new Rotation2d(angle));
    }

    /**
     * Velocity must be greater than zero <br>
     * untested with negative x Distance <br>
     * untested for negative y Distance (if you also want to shoot down) <br>
     * tested between angles of 0 < theta < 90 <br>
     * @param speed speed the projectile is moving at
     * @param deltaX the X distance between the starting and ending positions
     * @param deltaY the Y distance between the starting and ending positions
     * @return a {@link VelocityVector} with the calculated target angle
     */
    public static VelocityVector fromVelAndDist(double speed, double deltaX, double deltaY){
        double tanOfAngle = (
                ((deltaY >= 0) ? 1 : -1) * Math.sqrt(
                        Math.pow(deltaX,2) * (
                                (-1 * Math.pow(Constants.GRAVITY,2) * Math.pow(deltaX,2)) +
                                        (2 * Constants.GRAVITY * Math.pow(speed,2) * deltaY) +
                                        (Math.pow(speed,4))
                        )
                ) - (Math.pow(speed,2) * deltaX)) /
                (Constants.GRAVITY * Math.pow(deltaX,2));
        return new VelocityVector(speed,new Rotation2d(Math.atan(tanOfAngle)));
    }
    public static double combineComponents(double xSpeed, double ySpeed){
        return Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));
    }
}
