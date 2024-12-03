package frc.robot.subsystems.swervev3;


public class KinematicsConversionConfig {
     private final double wheelRadius;
     private final SwerveModuleProfile profile;

     public KinematicsConversionConfig(double wheelRadius, SwerveModuleProfile profile) {
          this.wheelRadius = wheelRadius;
          this.profile = profile;
     }

     public double getWheelRadius() {
          return wheelRadius;
     }

     public SwerveModuleProfile getProfile() {
          return profile;
     }
}
