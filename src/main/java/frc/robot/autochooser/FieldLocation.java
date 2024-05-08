package frc.robot.autochooser;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.RobotContainer;

import java.util.Arrays;
import java.util.HashMap;

public enum FieldLocation {
     SpeakerLeft(0.70,6.69,Math.toRadians(-120),"Speaker Left","Speaker Right"),
     SpeakFront(1.34,5.55,Math.PI,"Speaker Front","Speaker Front"),
     SpeakerRight(0.70,4.42,Math.toRadians(120),"Speaker Right","Speaker Left"),
     ZERO(0, 0,0,"Zero", "Zero"),
     INVALID(-1, -1,-1,"INVALID", "INVALID");
     private static final double RED_X_POS = 16.5; //meters
     private final double yPos;
     private final double xPose;
     private final double angle;
     private final String blueName;
     private final String redName;
     private static final HashMap<String, FieldLocation> nameMap = new HashMap<>();

     static{
          Arrays.stream(FieldLocation.values()).forEach(v -> nameMap.put(v.getShuffleboardName(), v));
     }

     FieldLocation(double xPos, double yPos,double angle, String blueName, String redName) {
          this.xPose = xPos;
          this.yPos = yPos;
          this.angle = angle;
          this.blueName = blueName;
          this.redName = redName;
     }

     public static FieldLocation fromName(String string) {
          return null;
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