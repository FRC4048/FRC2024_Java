package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;
import frc.robot.subsystems.swervev3.OdometryThread;
import frc.robot.subsystems.swervev3.bags.TimedGyroMeasurement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadedGyro {
    private final AHRS gyro;
    private final AtomicBoolean shouldReset = new AtomicBoolean(false);
    private final AtomicBoolean shouldOffset = new AtomicBoolean(false);
    private final LinkedTransferQueue<TimedGyroMeasurement> lastGyroMeasurements = new LinkedTransferQueue<>();
    private final AtomicLong gyroOffset = new AtomicLong();

    public ThreadedGyro(AHRS gyro) {
        this.gyro = gyro;
    }

    public void start(){
        OdometryThread.getInstance().addRunnable(time -> {
            if (shouldReset.get()) {
                gyro.reset();
                shouldReset.set(false);
            }
            if (shouldOffset.get()){
                gyro.setAngleAdjustment(Double.longBitsToDouble(gyroOffset.get()));
                shouldOffset.set(false);
            }
            updateGyro(time);
        });
    }

    private void updateGyro(double time) {
        lastGyroMeasurements.add(new TimedGyroMeasurement((gyro.getAngle() % 360 ) * -1, time));
    }

    public TimedGyroMeasurement getGyroValue(){
        return lastGyroMeasurements.peek();
    }

    public void resetGyro() {
        shouldReset.set(true);
    }

    public void setAngleAdjustment(double degrees) {
        gyroOffset.set(Double.doubleToLongBits(degrees));
        shouldOffset.set(true);
    }

    public List<TimedGyroMeasurement> flushRecentMeasurements(){
        List<TimedGyroMeasurement> measurementList = new ArrayList<>();
        lastGyroMeasurements.drainTo(measurementList);
        return measurementList;
    }

}
