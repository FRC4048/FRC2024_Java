package frc.robot.swervev2;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.constants.Constants;
import frc.robot.swervev2.components.EncodedSwerveTalon;
import frc.robot.swervev2.components.EncodedSwerveSparkMaxTalonHybrid;

/**
 * Utility class to build an {@link frc.robot.swervev2.components.EncodedSwerveSparkMax}
 * from a motorConfig and a Conversion Config <br>
 * @see frc.robot.swervev2.SwerveIdConfig
 * @see frc.robot.swervev2.KinematicsConversionConfig
 */
public class EncodedSwerveMotorBuilderSparkMaxTalonHybrid {
     private final SwerveIdConfig motorConfig;
     private final KinematicsConversionConfig conversionConfig;

     public EncodedSwerveMotorBuilderSparkMaxTalonHybrid(SwerveIdConfig motorConfig, KinematicsConversionConfig conversionConfig){
          this.motorConfig = motorConfig;
          this.conversionConfig = conversionConfig;
     }
     public EncodedSwerveSparkMaxTalonHybrid build(){
          CANSparkMax driveMotor = new CANSparkMax(motorConfig.getDriveMotorId(), CANSparkMax.MotorType.kBrushless);
          WPI_TalonSRX turnMotor = new WPI_TalonSRX(motorConfig.getTurnMotorId()); //uh idk if motorconfig works
          WPI_CANCoder canCoder = new WPI_CANCoder(motorConfig.getCanCoderId());
          double driveVelConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio() * 60);
          double drivePosConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio()); 
          double steerPosConvFactor = 2 * Math.PI / Constants.SWERVE_MODULE_PROFILE.getSteerRatio();
          return new EncodedSwerveSparkMaxTalonHybrid(driveMotor, turnMotor, canCoder, driveVelConvFactor, drivePosConvFactor, steerPosConvFactor);
     }
}
