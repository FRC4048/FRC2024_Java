package frc.robot.swervev2;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.robot.swervev2.components.EncodedSwerveSparkMax;
import frc.robot.swervev2.components.GenericEncodedSwerve;

public class EncodedSwerveMotorBuilder {
     private final SwerveIdConfig motorConfig;
     private final KinematicsConversionConfig conversionConfig;

     public EncodedSwerveMotorBuilder(SwerveIdConfig motorConfig, KinematicsConversionConfig conversionConfig){
          this.motorConfig = motorConfig;
          this.conversionConfig = conversionConfig;
     }
     public EncodedSwerveSparkMax build(){
          CANSparkMax driveMotor = new CANSparkMax(motorConfig.getDriveMotorId(), CANSparkMaxLowLevel.MotorType.kBrushless);
          CANSparkMax turnMotor = new CANSparkMax(motorConfig.getTurnMotorId(), CANSparkMaxLowLevel.MotorType.kBrushless);
          WPI_CANCoder canCoder = new WPI_CANCoder(motorConfig.getCanCoderId());
          double driveVelConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio() * 60);
          double drivePosConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio());
          double steerPosConvFactor = 2 * Math.PI / conversionConfig.getSteerGearRatio();
          return new EncodedSwerveSparkMax(driveMotor, turnMotor, canCoder, driveVelConvFactor, drivePosConvFactor, steerPosConvFactor);
     }
}
