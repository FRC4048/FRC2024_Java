package frc.robot.utils;

import com.kauailabs.navx.frc.AHRS;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadedGyro extends Thread{
    private final AHRS gyro;
    private final AtomicBoolean shouldReset = new AtomicBoolean(false);
    private final Phaser phaser;
    private volatile double lastGyro;
    private volatile double angleAdjustment;
    private boolean enabled = false;

    public ThreadedGyro(AHRS gyro, Phaser phaser) {
        this.gyro = gyro;
        this.phaser = phaser;
        this.angleAdjustment = gyro.getAngleAdjustment();

    }

    @Override
    public void start() {
        enabled = true;
    }

    @Override
    public void run() {
       while (enabled){
           phaser.arriveAndAwaitAdvance();
           gyro.setAngleAdjustment(getAngleAdjustment());
           if (shouldReset.get()) {
               gyro.reset();
               shouldReset.set(false);
           }
           updateGyro();
       }
    }

    private void updateGyro() {
        lastGyro = (gyro.getAngle() % 360)  * -1;
    }

    public double getGyroValue(){
        return lastGyro;
    }
    public void disable(){
        enabled = false;
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
