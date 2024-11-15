package frc.robot.swervev2.encoder;

import edu.wpi.first.wpilibj.AnalogEncoder;
import frc.robot.utils.math.AngleUtils;

public class SwerveAnalogAbsEncoder implements SwerveAbsEncoder {

    private final AnalogEncoder absEncoder;
    private double steerOffset;

    public SwerveAnalogAbsEncoder(AnalogEncoder absEncoder) {
        this.absEncoder = absEncoder;
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