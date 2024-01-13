package frc.robot.subsystems.swervev2;

public class KinematicsConversionConfig {
     private final double wheelRadius;
     private final double driveGearRatio;
     private final double steerGearRatio;

     public KinematicsConversionConfig(double wheelRadius, double driveGearRatio, double steerGearRatio) {
          this.wheelRadius = wheelRadius;
          this.driveGearRatio = driveGearRatio;
          this.steerGearRatio = steerGearRatio;
     }

     public double getWheelRadius() {
          return wheelRadius;
     }

     public double getDriveGearRatio() {
          return driveGearRatio;
     }

     public double getSteerGearRatio() {
          return steerGearRatio;
     }
}
