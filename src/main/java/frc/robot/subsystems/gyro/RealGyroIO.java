package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;
import frc.robot.utils.ThreadedGyro;
import frc.robot.utils.TimedGyroMeasurement;

import java.util.List;

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
    }

    @Override
    public void resetGyro() {
        gyro.resetGyro();
    }

    @Override
    public void updateInputs(GyroInputs inputs) {
        List<TimedGyroMeasurement> measurementList = gyro.flushRecentMeasurements();
        double[] anglesInDeg = new double[measurementList.size()];
        double[] anglesTimeStamps = new double[measurementList.size()];
        for (int i = 0; i < measurementList.size(); i++) {
            anglesInDeg[i] = measurementList.get(i).angle();
            anglesTimeStamps[i] = measurementList.get(i).time();
        }
        inputs.anglesInDeg = anglesInDeg;
        inputs.anglesTimeStamps = anglesTimeStamps;
        inputs.angleOffset = angleOffset;
    }
}
