package frc.robot.swervev2.components;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.constants.Constants;

public class EncodedSwerveTalon extends GenericEncodedSwerve {
    public EncodedSwerveTalon(WPI_TalonSRX driveMotor, WPI_TalonSRX steerMotor, WPI_CANCoder absEncoder, double driveVelFactor, double drivePosFactor, double steerPosFactor) {
        super(driveMotor, steerMotor, absEncoder, driveMotor.getSensorCollection(), steerMotor.getSensorCollection(), driveVelFactor, drivePosFactor, steerPosFactor);
        configureEncoders(driveVelFactor,drivePosFactor, steerPosFactor);
    }

    @Override
    public void configureEncoders(double driveVelFactor, double drivePosFactor, double steerPosFactor) {
        WPI_TalonSRX driveMotor = (WPI_TalonSRX) getDriveMotor();
        WPI_TalonSRX steerMotor = (WPI_TalonSRX) getSteerMotor();
        driveMotor.configFactoryDefault();
        steerMotor.configFactoryDefault();
        driveMotor.setNeutralMode(NeutralMode.Brake);
        steerMotor.setNeutralMode(NeutralMode.Brake);
        driveMotor.configPeakCurrentLimit(Constants.DRIVE_SMART_LIMIT);
        //driveMotor.setSecondaryCurrentLimit(Constants.DRIVE_SECONDARY_LIMIT);    Could not find
        driveMotor.configClosedloopRamp(Constants.DRIVE_RAMP_RATE_LIMIT);
        super.configureEncoders(driveVelFactor, drivePosFactor, steerPosFactor);
    }
}
