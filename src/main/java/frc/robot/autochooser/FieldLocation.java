package frc.robot.autochooser;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.RobotContainer;

public enum FieldLocation {
     SpeakerLeft(0.70,6.69,Math.toRadians(-120),"Speaker Left","Speaker Right"),
     SpeakFront(1.34,5.55,Math.PI,"Speaker Front","Speaker Front"),
     SpeakerRight(0.70,4.42,Math.toRadians(120),"Speaker Right","Speaker Left"),
     ZERO(0, 0,0,"Zero", "Zero");
     private static final double RED_X_POS = 16.5; //meters
     private final double yPos;
     private final double xPose;
     private final double angle;
     private final String blueName;
     private final String redName;

     FieldLocation(double xPos, double yPos,double angle, String blueName, String redName) {
          this.xPose = xPos;
          this.yPos = yPos;
          this.angle = angle;
          this.blueName = blueName;
          this.redName = redName;
     }
     public Pose2d getLocation(){
          double x = RobotContainer.isRedAlliance() ? RED_X_POS - xPose: xPose;
          double radian = RobotContainer.isRedAlliance() ? Math.PI-angle: angle;
          return new Pose2d(x, yPos, Rotation2d.fromRadians(radian));
     }

     public String getShuffleboardName(){
          return RobotContainer.isRedAlliance() ? redName : blueName;
     }

}