package frc.robot.autochooser;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.RobotContainer;

public enum FieldLocation {
     AmpSide(0.66,6.60,Math.toRadians(-120),"Amp Side"),
     SpeakFront(1.25,5.50,Math.PI,"Speaker Front"),
     FeederSide(0.66,4.50,Math.toRadians(120),"Feed Side"),
     ZERO(0, 0,0,"Zero");
     private static final double RED_X_POS = 16.5; //meters
     private final double yPos;
     private final double xPose;
     private final double angle;
     private final String name;

     FieldLocation(double xPos, double yPos,double angle, String name) {
          this.xPose = xPos;
          this.yPos = yPos;
          this.angle = angle;
          this.name = name;
     }
     public Pose2d getLocation(){
          double x = RobotContainer.isRedAlliance() ? RED_X_POS - xPose: xPose;
          double radian = RobotContainer.isRedAlliance() ? Math.PI-angle: angle;
          return new Pose2d(x, yPos, Rotation2d.fromRadians(radian));
     }

     public String getShuffleboardName(){
          return name;
     }

}