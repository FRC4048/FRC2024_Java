package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.Constants;

/**
 * Utility class for vectors used in projectile motion
 */
public class VectorUtils {
    /**
     * The projectile motion converts the given coords to 2d after applying the change in base coords from the arc provided.
     * Because shooter angle depends on position (not point math) and position depends on angle (not point math),
     * the function will approximate to a specified certainty by calling the function at 45 degrees,
     * getting that pose and then looping until the delta angle result is within a specified margin,
     * or you have exceeded the max iterations specified.
     * @param speed           the initial velocity of the projectile
     * @param centerArcX      the x cord of the arc created by the shooter moving at different angles
     * @param centerArcY      the y cord of the arc created by the shooter moving at different angles
     * @param centerArcZ      the z cord of the arc created by the shooter moving at different angles
     * @param arcRadius       the length of the shooter, which forms an arc when changing angle
     * @param destX           the target position in the x direction
     * @param destY           the target position in the y direction
     * @param destZ           the target position in the z direction
     * @param degreeThreshold threshold before angles are considered equal when iterating over positions angle relationship created by arc
     * @param maxIterations   the max amount of iterations before an angle is returned
     * @param direct          If true then deltaX must be less than or equal to the maxima of the trajectory parabola
     * @return a {@link VelocityVector} with the calculated target angle between 0 and 90 degrees.<br> <b>Impossible parameters will produce a null result</b>
     */
    public static VelocityVector fromArcAndDestAndVel(double speed, double centerArcX, double centerArcY, double centerArcZ, double arcRadius, double destX, double destY, double destZ, double degreeThreshold, int maxIterations, boolean direct) {
        double xDist = destX - centerArcX;
        double yDist = destY - centerArcY;
        VelocityVector lastVel = null;
        VelocityVector currVel = null;
        int i = 0;
        do {
            i++;
            double theta = Math.PI / 4;
            if (currVel != null) {
                lastVel = currVel;
                theta = currVel.getAngle().getRadians();
            }
            double rampXYOffset = (Math.cos(theta) * arcRadius);
            double rampZOffset = Math.sin(theta) * arcRadius;
            double xyDist = Math.hypot(xDist, yDist) - rampXYOffset;
            double z1 = centerArcZ + rampZOffset;
            double deltaZ = Math.abs(destZ - z1);
            currVel = fromVelAndDist(speed, xyDist, deltaZ, direct);
            System.out.println(xyDist);
        } while (i < maxIterations && (lastVel == null || currVel == null || Math.abs(lastVel.getAngle().getDegrees() - currVel.getAngle().getDegrees()) > degreeThreshold));
        return currVel;
    }
    /**
     * The projectile motion converts the given coords to 2d after applying the change in base coords from the arc provided.
     * Because shooter angle depends on position (not point math) and position depends on angle (not point math),
     * the function will approximate to a specified certainty by calling the function at 45 degrees,
     * getting that pose and then looping until the delta angle result is within 0.01 degrees,
     * or you have exceeded 7 iterations. <br>
     * To use custom certainty see {@link #fromArcAndDestAndVel(double, double, double, double, double, double, double, double, double, int, boolean)}
     * @param speed      the initial velocity of the projectile
     * @param centerArcX the x cord of the arc created by the shooter moving at different angles
     * @param centerArcY the y cord of the arc created by the shooter moving at different angles
     * @param centerArcZ the z cord of the arc created by the shooter moving at different angles
     * @param arcRadius  the length of the shooter, which forms an arc when changing angle
     * @param destX      the target position in the x direction
     * @param destY      the target position in the y direction
     * @param destZ     the target position in the z direction
     * @return a {@link VelocityVector} with the calculated target angle between 0 and 90 degrees.<br> <b>Impossible parameters will produce a null result</b>
     */
    public static VelocityVector fromArcAndDestAndVel(double speed, double centerArcX, double centerArcY, double centerArcZ, double arcRadius, double destX, double destY, double destZ) {
        return fromArcAndDestAndVel(speed, centerArcX, centerArcY, centerArcZ, arcRadius, destX, destY, destZ, 0.01, 7, true);
    }

    /**
     * @param speed  speed the projectile is moving at <br> <b>Must be > 0</b>
     * @param deltaX the X distance between the starting and ending positions <br> <b>Must be > 0</b>
     * @param deltaY the Y distance between the starting and ending positions <br> <b>Must be > 0</b>
     * @param direct If true then deltaX must be less than or equal to the maxima of the trajectory parabola
     * @return a {@link VelocityVector} with the calculated target angle between 0 and 90 degrees.<br> <b>Impossible parameters will produce a null result</b>
     */
    public static VelocityVector fromVelAndDist(double speed, double deltaX, double deltaY, boolean direct) {
        if (speed <= 0 || deltaX <= 0 || deltaY <= 0) return null;
        double tanOfAngle = (
                ((deltaY >= 0) ? 1 : -1) * Math.sqrt(
                        Math.pow(deltaX, 2) * (
                                (-1 * Math.pow(Constants.GRAVITY, 2) * Math.pow(deltaX, 2)) +
                                        (2 * Constants.GRAVITY * Math.pow(speed, 2) * deltaY) +
                                        (Math.pow(speed, 4))
                        )
                ) - (Math.pow(speed, 2) * deltaX)) /
                (Constants.GRAVITY * Math.pow(deltaX, 2));
        VelocityVector velocityVector = new VelocityVector(speed, new Rotation2d(Math.atan(tanOfAngle)));
        if (direct) return isDirect(velocityVector, deltaX) ? velocityVector : null;
        else return velocityVector;
    }

    /**
     * @param velocityVector the initial velocity and angle of the projectile
     * @return the X displacement of the projectile following a theoretical parabola with level starting and ending y values
     */
    public static double horizontalRange(VelocityVector velocityVector) {
        return ((Math.pow(velocityVector.getVelocity(), 2) * Math.sin(velocityVector.getAngle().getRadians() * 2))) / (-1 * Constants.GRAVITY);
    }

    private static boolean isDirect(VelocityVector velocityVector, double deltaX) {
        return deltaX <= horizontalRange(velocityVector) / 2;
    }

}
