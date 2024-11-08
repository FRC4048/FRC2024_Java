package frc.robot.swervev2;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.constants.Constants;
import frc.robot.swervev2.components.EncodedSwerveTalon;

/**
 * Utility class to build an {@link frc.robot.swervev2.components.EncodedSwerveSparkMax}
 * from a motorConfig and a Conversion Config <br>
 * @see frc.robot.swervev2.SwerveIdConfig
 * @see frc.robot.swervev2.KinematicsConversionConfig
 */
public class EncodedSwerveMotorBuilderTalon {
     private final SwerveIdConfig motorConfig;
     private final KinematicsConversionConfig conversionConfig;

     public EncodedSwerveMotorBuilderTalon(SwerveIdConfig motorConfig, KinematicsConversionConfig conversionConfig){
          this.motorConfig = motorConfig;
          this.conversionConfig = conversionConfig;
     }
     public EncodedSwerveTalon build(){
          WPI_TalonSRX driveMotor = new WPI_TalonSRX(motorConfig.getDriveMotorId());
          WPI_TalonSRX turnMotor = new WPI_TalonSRX(motorConfig.getTurnMotorId());
          WPI_CANCoder canCoder = new WPI_CANCoder(motorConfig.getCanCoderId());
          double driveVelConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio() * 60);
          double drivePosConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio());
          double steerPosConvFactor = 2 * Math.PI / Constants.SWERVE_MODULE_PROFILE.getSteerRatio();
          return new EncodedSwerveTalon(driveMotor, turnMotor, canCoder, driveVelConvFactor, drivePosConvFactor, steerPosConvFactor);
     }
}
