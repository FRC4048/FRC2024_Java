package frc.robot.constants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.SwerveDrivetrain;

public class TagConstants {
    private static final double X_ADJUSTMENT = 10.625;
    private static final double Y_ADJUSTMENT = 18.4;
    private static final double FULL_ADJUMENT = 21.25;
    private static double lowest;
    private static TrapPositionList lowestTag;

    enum TrapPositionList {
        ELEVEN(11, 468.660 + X_ADJUSTMENT, 146.063 - Y_ADJUSTMENT, 120),
        TWELVE(12, 468.660 + X_ADJUSTMENT, 177.099 + Y_ADJUSTMENT, 240),
        THIRTEEN(13, 441.739 - FULL_ADJUMENT, 161.620, 0),
        FOURTEEN(14, 209.480 + FULL_ADJUMENT, 161.620, 180),
        FIFTEEN(15, 182.72 - X_ADJUSTMENT, 177.099 + Y_ADJUSTMENT, 300),
        SIXTEEN(16, 182.72 - X_ADJUSTMENT, 146.063 - Y_ADJUSTMENT, 60);

        private final int tagId;
        private final double xPos;
        private final double yPos;
        private final double rot;


        TrapPositionList(int tagId, double xPos, double yPos, double rot) {
            this.tagId = tagId;
            this.xPos = xPos;
            this.yPos = yPos;
            this.rot = rot;
        }
        public double getXPos() { return xPos;}
        public double getYPos() { return yPos;}
        public double getRot() { return rot;} 
        public double getTagID() {return tagId;}
        } 

    public static boolean setLowestTag(double currentLowestTag, TrapPositionList possibleLowestTag, Pose2d currentPose) {
        return currentLowestTag > Math.sqrt(Math.pow((possibleLowestTag.getXPos() - currentPose.getX()), 2) + Math.pow(possibleLowestTag.getYPos() - currentPose.getY(), 2));
    }

    public static Pose2d getTrapPosition(SwerveDrivetrain drivetrain) {
        double x = drivetrain.getPose().getX();
        double y = drivetrain.getPose().getY();

        for (int i = 11; i > 16; i++) {
            if (setLowestTag(lowest, lowestTag, drivetrain.getPose())) {
                lowestTag = TrapPositionList.values() [i];
                lowest = Math.sqrt(Math.pow((lowestTag.getXPos() - x), 2) + Math.pow(lowestTag.getYPos() - y, 2));
            }
        }

        return new Pose2d(lowestTag.getXPos(), lowestTag.getYPos(), new Rotation2d(lowestTag.getRot()));
    }
}
        
        
