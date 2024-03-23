package frc.robot.constants;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.DoubleSubscriber;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class TagConstants {
    private static final double X_ADJUSTMENT = 10.625;
    private static final double Y_ADJUSTMENT = 18.4;
    private static final double FULL_ADJUMENT = 21.25;
    private static TrapPositionList lowestTag;
    private static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    private static NetworkTable table = inst.getTable("ROS");
    private static DoubleSubscriber subscriber = table.getDoubleTopic("apriltag_id").subscribe(0);

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

    public static void setLowestTag() {
        
        if (subscriber.get() == 11) {
            lowestTag = TrapPositionList.ELEVEN;
        }
        if (subscriber.get() == 12) {
            lowestTag = TrapPositionList.TWELVE;
        }
        if (subscriber.get() == 13) {
            lowestTag = TrapPositionList.THIRTEEN;
        }
        if (subscriber.get() == 14) {
            lowestTag = TrapPositionList.FOURTEEN;
        }
        if (subscriber.get() == 15) {
            lowestTag = TrapPositionList.FIFTEEN;
        }
        if (subscriber.get() == 16) {
            lowestTag = TrapPositionList.SIXTEEN;
        } else {
            lowestTag = null;
        }
        
    }

    public static Pose2d getTrapPosition() {
        setLowestTag();
        if (lowestTag != null) {
            return new Pose2d(lowestTag.getXPos() / Constants.METERS_TO_INCHES, lowestTag.getYPos() / Constants.METERS_TO_INCHES, new Rotation2d(lowestTag.getRot()));
        } else {
            return null;
        }
    }
}
        
        
