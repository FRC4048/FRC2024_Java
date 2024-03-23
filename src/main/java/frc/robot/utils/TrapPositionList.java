package frc.robot.utils;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.constants.Constants;

public enum TrapPositionList {

    ELEVEN(11, 468.660 + Constants.X_ADJUSTMENT, 146.063 - Constants.Y_ADJUSTMENT, 120),
    TWELVE(12, 468.660 + Constants.X_ADJUSTMENT, 177.099 + Constants.Y_ADJUSTMENT, 240),
    THIRTEEN(13, 441.739 - Constants.FULL_ADJUMENT, 161.620, 0),
    FOURTEEN(14, 209.480 + Constants.FULL_ADJUMENT, 161.620, 180),
    FIFTEEN(15, 182.72 - Constants.X_ADJUSTMENT, 177.099 + Constants.Y_ADJUSTMENT, 300),
    SIXTEEN(16, 182.72 - Constants.X_ADJUSTMENT, 146.063 - Constants.Y_ADJUSTMENT, 240);

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

    public Pose2d getTrapPosition() {
        return new Pose2d(getXPos() / Constants.METERS_TO_INCHES, getYPos() / Constants.METERS_TO_INCHES, Rotation2d.fromDegrees(getRot()));
    }

    public static TrapPositionList getTag(int tag) {
        return TrapPositionList.values()[tag - 11];
    }

    public double getXPos() {
        return xPos;
    }

    public double getYPos() {
        return yPos;
    }

    public double getRot() {
        return rot;
    }

    public double getTagID() {
        return tagId;
    }
}
