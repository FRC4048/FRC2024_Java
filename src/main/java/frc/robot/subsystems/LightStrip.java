package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;

public class LightStrip extends SubsystemBase {
    private final Spark colorSensorPort;
    private final AtomicReference<BlinkinPattern> pattern = new AtomicReference<>(BlinkinPattern.BLACK);
    private final Map<BooleanSupplier, BlinkinPattern> predicateLightEvents = new HashMap<>();
    private final AtomicBoolean running = new AtomicBoolean(true);
    public LightStrip(int port) {
        this.colorSensorPort = new Spark(port);
        new Thread(() -> {
            while (running.get()){
                for (BooleanSupplier supplier : predicateLightEvents.keySet()){
                    if (supplier.getAsBoolean()){
                        setPattern(predicateLightEvents.get(supplier));
                        predicateLightEvents.remove(supplier);
                        break;
                    }
                }
                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void setPattern(BlinkinPattern pattern) {
        this.pattern.set(pattern);
        colorSensorPort.set(pattern.getPwm());
        if (Constants.LED_DEBUG){
            SmartShuffleboard.put("Lightstrip", "pwm", pattern.getPwm());
            SmartShuffleboard.put("Lightstrip", "pattern", pattern.toString());
        }
    }

    public BlinkinPattern getPattern() {
        return pattern.get();
    }
    public void scheduleOnTrue(BooleanSupplier callable, BlinkinPattern pattern) {
        predicateLightEvents.put(callable, pattern);
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }
}
