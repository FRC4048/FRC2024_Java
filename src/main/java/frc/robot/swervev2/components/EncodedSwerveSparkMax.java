package frc.robot.swervev2.components;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;
import frc.robot.constants.Constants;

public class EncodedSwerveSparkMax extends GenericEncodedSwerve {
    public EncodedSwerveSparkMax(CANSparkMax driveMotor, CANSparkMax steerMotor, WPI_CANCoder absEncoder, double driveVelFactor, double drivePosFactor, double steerPosFactor) {
        super(driveMotor, steerMotor, absEncoder, driveMotor.getEncoder(), steerMotor.getEncoder(), driveVelFactor, drivePosFactor, steerPosFactor);
        configureEncoders(driveVelFactor,drivePosFactor, steerPosFactor);
    }

    @Override
    public void configureEncoders(double driveVelFactor, double drivePosFactor, double steerPosFactor) {
        CANSparkMax driveMotor = (CANSparkMax) getDriveMotor();
        CANSparkMax steerMotor = (CANSparkMax) getSteerMotor();
        driveMotor.restoreFactoryDefaults();
        steerMotor.restoreFactoryDefaults();
        driveMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        steerMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        driveMotor.setSmartCurrentLimit(Constants.DRIVE_SMART_LIMIT);
        driveMotor.setSecondaryCurrentLimit(Constants.DRIVE_SECONDARY_LIMIT);
        driveMotor.setClosedLoopRampRate(Constants.DRIVE_RAMP_RATE_LIMIT);
        super.configureEncoders(driveVelFactor, drivePosFactor, steerPosFactor);
    }
}
