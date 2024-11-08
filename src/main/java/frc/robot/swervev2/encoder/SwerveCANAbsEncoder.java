package frc.robot.swervev2.encoder;

import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.ctre.phoenix.sensors.WPI_CANCoder;
import frc.robot.utils.math.AngleUtils;

public class SwerveCANAbsEncoder implements SwerveAbsEncoder {

    private final WPI_CANCoder absEncoder;
    private double steerOffset;

    public SwerveCANAbsEncoder(WPI_CANCoder absEncoder) {
        this.absEncoder = absEncoder;
        absEncoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData,50);
    }

    @Override
    public double getAbsolutePosition() {
        return absEncoder.getAbsolutePosition();
    }

    @Override
    public void zero(double zeroAbs) {
        steerOffset = Math.toRadians(zeroAbs - absEncoder.getAbsolutePosition());
        steerOffset = AngleUtils.normalizeAngle2(steerOffset);
    }

    @Override
    public double getSteerOffset() {
        return steerOffset;
    }
}