package frc.robot.swervev2.components;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.constants.Constants;
import frc.robot.swervev2.encoder.SwerveSparkMaxEncoder;
import frc.robot.swervev2.encoder.SwerveTalonEncoder;

import com.revrobotics.CANSparkMax;

public class EncodedSwerveSparkMaxTalonHybrid extends GenericEncodedSwerve {
    public EncodedSwerveSparkMaxTalonHybrid(CANSparkMax driveMotor, WPI_TalonSRX steerMotor, WPI_CANCoder absEncoder, double driveVelFactor, double drivePosFactor, double steerPosFactor) {
        super(driveMotor, steerMotor, absEncoder, new SwerveSparkMaxEncoder(driveMotor, driveVelFactor, drivePosFactor), new SwerveTalonEncoder(steerMotor, driveVelFactor, steerPosFactor/60));
        configureEncoders();
    }

    @Override
    public void configureEncoders() {
        CANSparkMax driveMotor = (CANSparkMax) getDriveMotor();
        WPI_TalonSRX steerMotor = (WPI_TalonSRX) getSteerMotor();
        driveMotor.restoreFactoryDefaults();
        steerMotor.configFactoryDefault();
        driveMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        steerMotor.setNeutralMode(NeutralMode.Brake);
        //driveMotor.setSmartCurrentLimit(Constants.DRIVE_SMART_LIMIT); we do not know and noah voodoo
        //driveMotor.setSecondaryCurrentLimit(Constants.DRIVE_SECONDARY_LIMIT);
        //driveMotor.setClosedLoopRampRate(Constants.DRIVE_RAMP_RATE_LIMIT);
        super.configureEncoders();
    }
}
