package frc.robot.utils;

import com.kauailabs.navx.frc.AHRS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadedGyro {
    private final AHRS gyro;
    private final AtomicBoolean shouldReset = new AtomicBoolean(false);
    private final AtomicBoolean shouldOffset = new AtomicBoolean(false);
    private final AtomicLong lastGyro;
    private final AtomicLong angleAdjustment;
    private final AtomicLong gyroOffset = new AtomicLong();
    private final ScheduledExecutorService executor;

    public ThreadedGyro(AHRS gyro) {
        this.gyro = gyro;
        this.angleAdjustment = new AtomicLong(Double.doubleToLongBits(0));
        this.lastGyro = new AtomicLong((Double.doubleToLongBits(getGyroValue())));
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public void start(){
        executor.scheduleAtFixedRate(() -> {
            if (shouldOffset.get()){
                gyro.setAngleAdjustment(Double.longBitsToDouble(angleAdjustment.get()));
                shouldReset.set(false);
            }
            if (shouldReset.get()) {
                gyro.reset();
                shouldReset.set(false);
            }
            updateGyro();
        },0,10, TimeUnit.MILLISECONDS);
    }

    private void updateGyro() {
        lastGyro.set(Double.doubleToLongBits(((gyro.getAngle()) % 360)  * -1));
    }

    public double getGyroValue(){
        return Double.longBitsToDouble(lastGyro.get());
    }

    public void resetGyro() {
        shouldReset.set(true);
    }

    public void setAngleAdjustment(double degrees) {
        gyroOffset.set(Double.doubleToLongBits(degrees));
        shouldOffset.set(true);
    }
}
