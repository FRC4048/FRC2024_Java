package frc.robot.subsystems.swervev3.io.steer;

import com.revrobotics.CANSparkMax;
import frc.robot.subsystems.swervev3.KinematicsConversionConfig;

public class SparkMaxSteerMotorIO implements SwerveSteerMotorIO{
    private final CANSparkMax steerMotor;

    public SparkMaxSteerMotorIO(int steerMotorId, KinematicsConversionConfig conversionConfig, boolean steerInverted) {
        steerMotor = new CANSparkMax(steerMotorId, CANSparkMax.MotorType.kBrushless);
        setMotorConfig(steerInverted);
        setConversionFactors(conversionConfig);
        resetEncoder();

    }

    private void setConversionFactors(KinematicsConversionConfig conversionConfig) {
        double steerPosConvFactor = 2 * Math.PI / conversionConfig.getProfile().getSteerGearRatio();
        steerMotor.getEncoder().setPositionConversionFactor(steerPosConvFactor);
        steerMotor.getEncoder().setVelocityConversionFactor(steerPosConvFactor / 60);
    }

    private void setMotorConfig(boolean steerInverted) {
        steerMotor.restoreFactoryDefaults();
        steerMotor.setIdleMode(CANSparkMax.IdleMode.kBrake);
        steerMotor.setInverted(steerInverted);
    }

    @Override
    public void setSteerVoltage(double volts) {
        steerMotor.setVoltage(volts);
    }

    @Override
    public void resetEncoder() {
        steerMotor.getEncoder().setPosition(0);
    }

    @Override
    public void updateInputs(SwerveSteerMotorInput input) {
        input.steerEncoderPosition = steerMotor.getEncoder().getPosition();
        input.steerEncoderVelocity = steerMotor.getEncoder().getVelocity();
    }
}
