package frc.robot.subsystems.swervev3.io.steer;

import com.revrobotics.CANSparkMax;
import frc.robot.subsystems.swervev3.KinematicsConversionConfig;

import java.util.concurrent.atomic.AtomicLong;

public class SparkMaxSteerMotorIO implements SwerveSteerMotorIO{
    private final CANSparkMax steerMotor;
    private final AtomicLong steerOffset = new AtomicLong(0);

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
    public void setSteerOffset(double zeroAbs, double absCurrentPose) {
        steerMotor.getEncoder().setPosition(0);
        steerOffset.set(Double.doubleToLongBits(normalizeAngle(Math.toRadians(zeroAbs - absCurrentPose))));
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
        steerMotor.getEncoder().setPosition(0);
    }

    @Override
    public void updateInputs(SwerveSteerMotorInput input) {
        input.steerEncoderPosition = normalizeAngle(steerMotor.getEncoder().getPosition() - Double.longBitsToDouble(steerOffset.get()));
        input.steerEncoderVelocity = steerMotor.getEncoder().getVelocity();
        input.steerOffset = Double.longBitsToDouble(steerOffset.get());
    }
}
