package frc.robot.utils;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FeederUpdateThread {
    private final Feeder feeder;
    private final Runnable runnable;
    private final ScheduledThreadPoolExecutor executor;
    public FeederUpdateThread(int corePoolSize, Feeder feeder) {
        this.feeder = feeder;
        this.executor = new ScheduledThreadPoolExecutor(corePoolSize);
        this.runnable = () -> {
            if (feeder.updateColorSensor()){
                updateMotorForceStop();
            }
        };
    }
    private void updateMotorForceStop() {
        if (feeder.isListeningForceStop() && Feeder.shouldStop(true, feeder.getMaxConfidence())){
            feeder.setForceStop(true);
            feeder.setFeederMotorSpeed(0);
        } else {
            feeder.setForceStop(false);
        }
    }
    public void start(){
        executor.scheduleAtFixedRate(runnable,0, Constants.COLOR_SENSOR_UPDATE_RATE_MILLS,TimeUnit.MILLISECONDS);
        executor.close();
    }

}
