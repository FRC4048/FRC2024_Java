package frc.robot.subsystems.swervev3.io;

import com.ctre.phoenix.sensors.WPI_CANCoder;
import com.revrobotics.CANSparkMax;
import frc.robot.constants.Constants;
import frc.robot.subsystems.swervev3.OdometryThread;
import frc.robot.subsystems.swervev3.bags.ModuleInputsStamped;
import frc.robot.swervev2.KinematicsConversionConfig;
import frc.robot.swervev2.SwerveIdConfig;
import org.littletonrobotics.junction.Logger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class SparkMaxModuleIO implements ModuleIO {
    private final CANSparkMax driveMotor;
    private final CANSparkMax steerMotor;
    private final WPI_CANCoder absEncoder;
    private double steerOffset;
    private final Queue<ModuleInputsStamped> moduleReadingQueue = new LinkedList<>();
    private final ReentrantLock queueLock = new ReentrantLock();

    public SparkMaxModuleIO(SwerveIdConfig motorConfig, KinematicsConversionConfig conversionConfig, boolean driveInverted, boolean steerInverted) {
        driveMotor = new CANSparkMax(motorConfig.getDriveMotorId(), CANSparkMax.MotorType.kBrushless);
        steerMotor = new CANSparkMax(motorConfig.getTurnMotorId(), CANSparkMax.MotorType.kBrushless);
        absEncoder = new WPI_CANCoder(motorConfig.getCanCoderId());
        setMotorConfig(driveInverted, steerInverted);
        setConversionFactors(conversionConfig);
        OdometryThread.getInstance().addRunnable(time -> {
            ModuleInputsStamped input = new ModuleInputsStamped(
                    steerMotor.getEncoder().getPosition(),
                    driveMotor.getEncoder().getPosition(),
                    driveMotor.getEncoder().getVelocity(),
                    steerMotor.getEncoder().getVelocity(),
                    time
            );
            queueLock.lock();
            moduleReadingQueue.add(input);
            queueLock.unlock();
        });
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
        OdometryThread.getInstance().getLock().lock();
        driveMotor.setVoltage(volts);
        OdometryThread.getInstance().getLock().unlock();
    }

    @Override
    public void setSteerVoltage(double volts) {
        OdometryThread.getInstance().getLock().lock();
        steerMotor.setVoltage(volts);
        OdometryThread.getInstance().getLock().unlock();
    }

    @Override
    public void setSteerOffset(double zeroAbs) {
        OdometryThread.getInstance().getLock().lock();
        steerMotor.getEncoder().setPosition(0);
        steerOffset = Math.toRadians(zeroAbs - absEncoder.getAbsolutePosition());
        steerOffset = normalizeAngle(steerOffset);
        OdometryThread.getInstance().getLock().unlock();
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
        OdometryThread.getInstance().getLock().lock();
        driveMotor.getEncoder().setPosition(0);
        steerMotor.getEncoder().setPosition(0);
        OdometryThread.getInstance().getLock().unlock();
    }

    @Override
    public void updateInputs(SwerveModuleInput input) {
        queueLock.lock();
        double size = moduleReadingQueue.size();
        Logger.recordOutput("ModuleReadingsSize", size);
        input.steerEncoderPosition = new double[moduleReadingQueue.size()];
        input.driveEncoderPosition = new double[moduleReadingQueue.size()];
        input.driveEncoderVelocity = new double[moduleReadingQueue.size()];
        input.steerEncoderVelocity = new double[moduleReadingQueue.size()];
        input.measurementTimestamps = new double[moduleReadingQueue.size()];
        ModuleInputsStamped poll = moduleReadingQueue.poll();
        int i = 0;
        while (poll != null) {
            input.steerEncoderPosition[i] = poll.steerEncoderPosition();
            input.driveEncoderPosition[i] = poll.driveEncoderPosition();
            input.driveEncoderVelocity[i] = poll.driveEncoderVelocity();
            input.steerEncoderVelocity[i] = poll.steerEncoderVelocity();
            input.measurementTimestamps[i] = poll.measurementTimestamp();
            poll = moduleReadingQueue.poll();
        }
        OdometryThread.getInstance().getLock().lock();
        input.driveCurrentDraw = driveMotor.getOutputCurrent();
        input.steerOffset = steerOffset;
        OdometryThread.getInstance().getLock().unlock();
        queueLock.unlock();
    }
}
