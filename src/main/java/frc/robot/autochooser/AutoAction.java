package frc.robot.autochooser;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public enum AutoAction {
    DoNothing("Do Nothing", 0, 0, 0),
    FigureEight("Figure 8", 0, 0, 0),
    CrossLine("Cross The Line", 30, 108, 186),
    TwoPieceMoveLeft("Two Piece Move Left", 20, 108, 150),
    OnePieceMoveLeft("One Piece Move Left", 20, 108, 150);

    private final int rightLocationY;
    private final int leftLocationY;
    private final int middleLocationY;
    private final String name;
    private static final int RED_X_POS = (int) Math.round(Units.Meters.convertFrom(Constants.FIELD_LENGTH_X_FEET, Units.Feet));

    AutoAction(String name, int rightLocationY, int middleLocationY, int leftLocationY) {
        this.name = name;
        this.rightLocationY = rightLocationY;
        this.leftLocationY = leftLocationY;
        this.middleLocationY = middleLocationY;
    }

    public Translation2d getRightLocation() {
        int x = RobotContainer.shouldFlip() ? RED_X_POS : 0;
        return new Translation2d(x, rightLocationY);
    }

    public Translation2d getLeftLocation() {
        int x = RobotContainer.shouldFlip() ? RED_X_POS : 0;
        return new Translation2d(x, leftLocationY);
    }

    public Translation2d getMiddleLocation() {
        int x = RobotContainer.shouldFlip() ? RED_X_POS : 0;
        return new Translation2d(x, middleLocationY);
    }

    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return getName();
    }
}
