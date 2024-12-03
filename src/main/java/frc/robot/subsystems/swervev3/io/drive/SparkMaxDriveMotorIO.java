package frc.robot.subsystems.swervev3.io.drive;

import com.revrobotics.CANSparkMax;
import frc.robot.constants.Constants;
import frc.robot.subsystems.swervev3.KinematicsConversionConfig;

public class SparkMaxDriveMotorIO implements SwerveDriveMotorIO {

    private final CANSparkMax driveMotor;

    public SparkMaxDriveMotorIO(int driveMotorIO, KinematicsConversionConfig conversionConfig, boolean driveInverted) {
        driveMotor = new CANSparkMax(driveMotorIO, CANSparkMax.MotorType.kBrushless);
        setMotorConfig(driveInverted);
        setConversionFactors(conversionConfig);
    }

    @Override
    public void setDriveVoltage(double volts) {
        driveMotor.setVoltage(volts);
    }

    private void setConversionFactors(KinematicsConversionConfig conversionConfig) {
        double driveVelConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getProfile().getDriveGearRatio() * 60);
        double drivePosConvFactor = (2 * conversionConfig.getWheelRadius() * Math.PI) / (conversionConfig.getProfile().getDriveGearRatio());
        driveMotor.getEncoder().setVelocityConversionFactor(driveVelConvFactor);
        driveMotor.getEncoder().setPositionConversionFactor(drivePosConvFactor);
    }

    private void setMotorConfig(boolean driveInverted) {
        driveMotor.restoreFactoryDefaults();
        driveMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        driveMotor.setSmartCurrentLimit(Constants.DRIVE_SMART_LIMIT);
        driveMotor.setSecondaryCurrentLimit(Constants.DRIVE_SECONDARY_LIMIT);
        driveMotor.setClosedLoopRampRate(Constants.DRIVE_RAMP_RATE_LIMIT);
        driveMotor.setInverted(driveInverted);
    }

    @Override
    public void resetEncoder() {
        driveMotor.getEncoder().setPosition(0);
    }

    @Override
    public void updateInputs(SwerveDriveMotorInput input) {
        input.driveEncoderPosition = driveMotor.getEncoder().getPosition();
        input.driveEncoderVelocity = driveMotor.getEncoder().getVelocity();
        input.driveCurrentDraw = driveMotor.getOutputCurrent();
    }
}
