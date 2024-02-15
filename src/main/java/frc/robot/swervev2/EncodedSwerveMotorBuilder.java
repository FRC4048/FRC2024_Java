package frc.robot.swervev2;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;
import frc.robot.constants.Constants;
import frc.robot.swervev2.components.EncodedSwerveSparkMax;

/**
 * Utility class to build an {@link frc.robot.swervev2.components.EncodedSwerveSparkMax}
 * from a motorConfig and a Conversion Config <br>
 * @see frc.robot.swervev2.SwerveIdConfig
 * @see frc.robot.swervev2.KinematicsConversionConfig
 */
public class EncodedSwerveMotorBuilder {
     private final SwerveIdConfig motorConfig;
     private final KinematicsConversionConfig conversionConfig;

     public EncodedSwerveMotorBuilder(SwerveIdConfig motorConfig, KinematicsConversionConfig conversionConfig){
          this.motorConfig = motorConfig;
          this.conversionConfig = conversionConfig;
     }
     public EncodedSwerveSparkMax build(){
          CANSparkMax driveMotor = new CANSparkMax(motorConfig.getDriveMotorId(), CANSparkMax.MotorType.kBrushless);
          CANSparkMax turnMotor = new CANSparkMax(motorConfig.getTurnMotorId(), CANSparkMax.MotorType.kBrushless);
          WPI_CANCoder canCoder = new WPI_CANCoder(motorConfig.getCanCoderId());
          double driveVelConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio() * 60);
          double drivePosConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio());
          double steerPosConvFactor = 2 * Math.PI / Constants.SWERVE_MODULE_PROFILE.getSteerRatio();
          return new EncodedSwerveSparkMax(driveMotor, turnMotor, canCoder, driveVelConvFactor, drivePosConvFactor, steerPosConvFactor);
     }
}
