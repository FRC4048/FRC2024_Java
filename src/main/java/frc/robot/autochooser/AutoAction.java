package frc.robot.autochooser;

import edu.wpi.first.wpilibj.DriverStation;

import java.security.InvalidParameterException;

public enum AutoAction {
     DoNothing("Do Nothing",0,0,0),
     CrossLine("Cross The Line",30,108,186),
     TwoPieceMoveLeft("Two Piece Move Left",20,108,150),
     OnePieceMoveLeft("One Piece Move Left",20,108,150);

     private final int rightLocation;
     private final int leftLocation;
     private final int middleLocation;
     private final String name;
     private static final int RED_ALLIANCE_YPOS = 99;
     private static final int BLUE_ALLIANCE_YPOS = 0;

     AutoAction(String name,int rightLocation,int middleLocation, int leftLocation) {
          this.name = name;
          this.rightLocation = rightLocation;
          this.leftLocation = leftLocation;
          this.middleLocation = middleLocation;
     }

     public int getRightLocation() {
          return rightLocation;
     }

     public int getLeftLocation() {
          return leftLocation;
     }

     public int getMiddleLocation() {
          return middleLocation;
     }

     public String getName() {
          return name;
     }

     public int getNormalizedYCord(FieldLocation location, DriverStation.Alliance alliance){
          int y = alliance.equals(DriverStation.Alliance.Red) ? RED_ALLIANCE_YPOS : BLUE_ALLIANCE_YPOS;
          switch (location){
               case Left:
                    return y + getLeftLocation();
               case Right:
                    return y + getRightLocation();
               case Middle:
                    if (getMiddleLocation() !=-1) return y + getMiddleLocation();
               default: throw new InvalidParameterException(String.format("FieldLocation must be of types {%s,%s,%s} and location must not be -1",FieldLocation.Left,FieldLocation.Right,FieldLocation.Middle));
          }
     }

     @Override
     public String toString() {
          return getName();
     }
}
