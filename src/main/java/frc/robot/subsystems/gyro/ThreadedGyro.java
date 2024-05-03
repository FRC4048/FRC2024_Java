package frc.robot.subsystems.gyro;

import com.kauailabs.navx.frc.AHRS;
import frc.robot.swervev3.OdometryThread;
import frc.robot.utils.TimedGyroMeasurement;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadedGyro {
    private final AHRS gyro;
    private final AtomicBoolean shouldReset = new AtomicBoolean(false);
    private final AtomicBoolean shouldOffset = new AtomicBoolean(false);
    private final Queue<TimedGyroMeasurement> lastGyroMeasurements = new LinkedList<>();
    private final ReentrantLock gyroLock = new ReentrantLock();
    private final AtomicLong gyroOffset = new AtomicLong();

    public ThreadedGyro(AHRS gyro) {
        this.gyro = gyro;
    }

    public void start(){
        updateGyro(Logger.getRealTimestamp());
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
        gyroLock.lock();
        lastGyroMeasurements.add(new TimedGyroMeasurement((gyro.getAngle() % 360 ) * -1, Logger.getRealTimestamp()));
        gyroLock.unlock();
    }

    public TimedGyroMeasurement getGyroValue(){
        gyroLock.lock();
        TimedGyroMeasurement peek = lastGyroMeasurements.peek();
        gyroLock.unlock();
        return peek;
    }

    public void resetGyro() {
        shouldReset.set(true);
    }

    public void setAngleAdjustment(double degrees) {
        gyroOffset.set(Double.doubleToLongBits(degrees));
        shouldOffset.set(true);
    }

    public List<TimedGyroMeasurement> flushRecentMeasurements(){
        gyroLock.lock();
        List<TimedGyroMeasurement> measurementList = new ArrayList<>();
        TimedGyroMeasurement measurement = lastGyroMeasurements.poll();
        while (measurement != null){
            measurementList.add(measurement);
            measurement = lastGyroMeasurements.poll();
        }
        gyroLock.unlock();
        return measurementList;
    }

}
