package frc.robot.subsystems.swervev3.io;

import com.ctre.phoenix6.hardware.CANcoder;
import com.revrobotics.CANSparkMax;
import frc.robot.constants.Constants;
import frc.robot.swervev2.KinematicsConversionConfig;
import frc.robot.swervev2.SwerveIdConfig;

import java.util.concurrent.atomic.AtomicLong;

public class SparkMaxModuleIO implements ModuleIO {
    private final CANSparkMax driveMotor;
    private final CANSparkMax steerMotor;
    private final CANcoder absEncoder;
    private final AtomicLong steerOffset = new AtomicLong(0);

    public SparkMaxModuleIO(SwerveIdConfig motorConfig, KinematicsConversionConfig conversionConfig, boolean driveInverted, boolean steerInverted) {
        driveMotor = new CANSparkMax(motorConfig.getDriveMotorId(), CANSparkMax.MotorType.kBrushless);
        steerMotor = new CANSparkMax(motorConfig.getTurnMotorId(), CANSparkMax.MotorType.kBrushless);
        absEncoder = new CANcoder(motorConfig.getCanCoderId());
        setMotorConfig(driveInverted, steerInverted);
        setConversionFactors(conversionConfig);
        resetEncoder();
    }

    private void setConversionFactors(KinematicsConversionConfig conversionConfig) {
        double driveVelConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio() * 60);
        double drivePosConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getDriveGearRatio());
        double steerPosConvFactor = 2 * Math.PI / Constants.SWERVE_MODULE_PROFILE.getSteerRatio();
        driveMotor.getEncoder().setVelocityConversionFactor(driveVelConvFactor);
        driveMotor.getEncoder().setPositionConversionFactor(drivePosConvFactor);
        steerMotor.getEncoder().setPositionConversionFactor(steerPosConvFactor);
        steerMotor.getEncoder().setVelocityConversionFactor(steerPosConvFactor / 60);
    }

    private void setMotorConfig(boolean driveInverted, boolean steerInverted) {
        driveMotor.restoreFactoryDefaults();
        steerMotor.restoreFactoryDefaults();
        driveMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        steerMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        driveMotor.setSmartCurrentLimit(Constants.DRIVE_SMART_LIMIT);
        driveMotor.setSecondaryCurrentLimit(Constants.DRIVE_SECONDARY_LIMIT);
        driveMotor.setClosedLoopRampRate(Constants.DRIVE_RAMP_RATE_LIMIT);
        driveMotor.setInverted(driveInverted);
        steerMotor.setInverted(steerInverted);
    }

    @Override
    public void setDriveVoltage(double volts) {
        driveMotor.setVoltage(volts);
    }

    @Override
    public void setSteerVoltage(double volts) {
        steerMotor.setVoltage(volts);
    }

    @Override
    public void setSteerOffset(double zeroAbs) {
        steerMotor.getEncoder().setPosition(0);
        steerOffset.set(Double.doubleToLongBits(normalizeAngle(Math.toRadians(zeroAbs - absEncoder.getAbsolutePosition().getValueAsDouble()))));
    }

    private double normalizeAngle(double angleInRad) {
        angleInRad %= 2 * Math.PI;
        if (angleInRad < 0) {
            angleInRad += 2 * Math.PI;
        }
        return angleInRad;
    }

    @Override
    public void resetEncoder() {
        driveMotor.getEncoder().setPosition(0);
        steerMotor.getEncoder().setPosition(0);
    }

    @Override
    public void updateInputs(SwerveModuleInput input) {
        input.steerEncoderPosition = normalizeAngle(steerMotor.getEncoder().getPosition() - Double.longBitsToDouble(steerOffset.get()));
        input.driveEncoderPosition = driveMotor.getEncoder().getPosition();
        input.driveEncoderVelocity = driveMotor.getEncoder().getVelocity();
        input.steerEncoderVelocity = steerMotor.getEncoder().getVelocity();
        input.driveCurrentDraw = driveMotor.getOutputCurrent();
        input.steerOffset = Double.longBitsToDouble(steerOffset.get());
    }
}
