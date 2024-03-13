package frc.robot.utils;

import com.revrobotics.ColorMatchResult;
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
            ColorMatchResult matchedColor = feeder.getMatchedColor();
            if (matchedColor.confidence > feeder.getMaxConfidence()) {
                feeder.setMaxConfidence((int) (matchedColor.confidence * 100));
                updateMotorForceStop();
            }
        };
    }
    private void updateMotorForceStop() {
        if (feeder.isListeningForceStop() && Feeder.shouldStop(true, feeder.getMaxConfidence())){
            feeder.setListeningForceStop(true);
            feeder.setFeederMotorSpeed(0);
        }
    }
    public void start(){
        executor.scheduleAtFixedRate(runnable,0, Constants.COLOR_SENSOR_UPDATE_RATE_MILLS,TimeUnit.MILLISECONDS);
        executor.close();
    }

}
