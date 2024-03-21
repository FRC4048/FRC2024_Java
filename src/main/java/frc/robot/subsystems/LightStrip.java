package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.BlinkinPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class LightStrip extends SubsystemBase {
    private static final String baseLogName = "/robot/lightstrip/";
    private final Spark colorSensorPort;
    private BlinkinPattern pattern = BlinkinPattern.BLACK;
    private final Map<BooleanSupplier, BlinkinPattern> predicateLightEvents = new HashMap<>();
    public LightStrip(int port) {
        this.colorSensorPort = new Spark(port);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> predicateLightEvents.keySet()
                .stream()
                .filter(BooleanSupplier::getAsBoolean)
                .findFirst()
                .ifPresent(c -> {
                            setPattern(predicateLightEvents.get(c));
                            predicateLightEvents.remove(c);
                        }
                ),0,40, TimeUnit.MILLISECONDS);
    }

    public synchronized void setPattern(BlinkinPattern pattern) {
        this.pattern = pattern;
        colorSensorPort.set(pattern.getPwm());
    }

    public synchronized BlinkinPattern getPattern() {
        return pattern;
    }
    public void scheduleOnTrue(BooleanSupplier callable, BlinkinPattern pattern) {
        predicateLightEvents.put(callable, pattern);
    }
}
