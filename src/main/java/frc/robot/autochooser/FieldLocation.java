package frc.robot.autochooser;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.RobotContainer;

public enum FieldLocation {
     Right(0),
     Middle(0),
     Left(0);
     private static final double RED_X_POS = 16.5; //meters
     private final double yPos;

     FieldLocation(double yPos) {
          this.yPos = yPos;
     }
     public Translation2d getLocation(){
          double x = RobotContainer.shouldFlip() ? RED_X_POS : 0;
          return new Translation2d(x, yPos);
     }
}
