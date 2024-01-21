package frc.robot.autochooser;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.RobotContainer;

public enum FieldLocation {
     SpeakerLeft(0.66,4.50),
     SpeakFront(1.25,5.50),
     SpeakerRight(0.66,6.60);
     private static final double RED_X_POS = 16.5; //meters
     private final double yPos;
     private final double xPose;

     FieldLocation(double xPos, double yPos) {
          this.xPose = xPos;
          this.yPos = yPos;
     }
     public Translation2d getLocation(){
          double x = RobotContainer.shouldFlip() ? RED_X_POS - xPose * 2: xPose;
          return new Translation2d(x, yPos);
     }
}
