package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;

public class RealGyroIO implements GyroIO {
    private final ThreadedGyro gyro;
    private double angleOffset = 0;

    public RealGyroIO() {
        this.gyro = new ThreadedGyro(new AHRS());
        gyro.start();
    }

    @Override
    public void setAngleOffset(double offset) {
        this.angleOffset = offset;
        gyro.setAngleAdjustment(angleOffset);
    }

    @Override
    public void resetGyro() {
        gyro.resetGyro();
    }

    @Override
    public void updateInputs(GyroInputs inputs) {
        inputs.anglesInDeg = gyro.getGyroValue();
        inputs.angleOffset = angleOffset;
    }
}
