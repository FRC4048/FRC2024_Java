package frc.robot.utils;

import com.kauailabs.navx.frc.AHRS;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadedGyro {
    private final AHRS gyro;
    private final AtomicBoolean shouldReset = new AtomicBoolean(false);
    private volatile double lastGyro;
    private volatile double angleAdjustment;
    private boolean enabled = false;
    private final ScheduledExecutorService executor;

    public ThreadedGyro(AHRS gyro) {
        this.gyro = gyro;
        this.angleAdjustment = gyro.getAngleAdjustment();
        this.executor = Executors.newScheduledThreadPool(1);
    }

    public void execute(){
        executor.scheduleAtFixedRate(() -> {
            gyro.setAngleAdjustment(getAngleAdjustment());
            if (shouldReset.get()) {
                gyro.reset();
                shouldReset.set(false);
            }
            updateGyro();
        },0,10, TimeUnit.MILLISECONDS);
    }

    private void updateGyro() {
        lastGyro = (gyro.getAngle() % 360)  * -1;
    }

    public double getGyroValue(){
        return lastGyro;
    }

    public void updateIfDisabled() {
        if (!enabled){
            updateGyro();
        }
    }

    public void resetGyro() {
        shouldReset.set(true);
    }

    public void setAngleAdjustment(double degrees) {
        angleAdjustment = degrees;
    }
    public double getAngleAdjustment(){
        return angleAdjustment;
    }
}
