package frc.robot.utils.math;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.Constants;

/**
 * Utility class for vectors used in projectile motion
 */
public class VectorUtils {
    public static VelocityVector fromArcAndDestAndVel(double speed, double centerArcX, double centerArcY, double arcRadius, double destX, double destY, double degreeThreshold, int maxIterations, boolean direct){
        VelocityVector lastVel = null;
        VelocityVector currVel = null;
        int i = 0;
        do {
            i++;
            double positionAngle = 0;
            if (currVel != null){
                lastVel = currVel;
                positionAngle = currVel.getAngle().getRadians();
            }
            double x1 = centerArcX + Math.cos(positionAngle) * arcRadius;
            double y1 = centerArcY + Math.sin(positionAngle) * arcRadius;
            double deltaX = Math.abs(destX - x1);
            double deltaY = Math.abs(destY - y1);
            currVel = fromVelAndDist(speed, deltaX, deltaY, direct);
        } while (i < maxIterations && (lastVel == null || currVel == null || Math.abs(lastVel.getAngle().getDegrees() - currVel.getAngle().getDegrees()) > degreeThreshold));
        return currVel;
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
