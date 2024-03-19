package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.logging.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class LightStrip extends SubsystemBase {
    private static final String baseLogName = "/robot/lightstrip/";
    private final Spark colorSensorPort;
    private BlinkinPattern pattern = BlinkinPattern.WHITE;
    private Map<Double, BlinkinPattern> delayedLightEvents = new TreeMap<>(Double::compareTo);
    private Map<Callable<Boolean>, BlinkinPattern> predicateLightEvents = new HashMap<>();

    public LightStrip(int port) {
        this.colorSensorPort = new Spark(port);
    }

    @Override
    public void periodic() {
        delayedLightEvents.keySet().stream()
                .filter(t -> t - Timer.getFPGATimestamp() <= TimeUnit.MILLISECONDS.convert(20,TimeUnit.SECONDS))
                .findFirst()
                .ifPresentOrElse(t -> {
                    setPattern(delayedLightEvents.get(t));
                    delayedLightEvents.remove(t);
                }, ()->{});
        predicateLightEvents.keySet().stream().filter(c -> {
            try {
                return c.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).findFirst().ifPresentOrElse(c -> {
            setPattern(predicateLightEvents.get(c));
            predicateLightEvents.remove(c);
        }, ()->{});
    }

    public void setPattern(BlinkinPattern pattern) {
        this.pattern = pattern;
        colorSensorPort.set(pattern.getPwm());
        SmartDashboard.putString("PATTERN",pattern.toString());
        Logger.logDouble(baseLogName + "pwmSignal", pattern.getPwm(), Constants.ENABLE_LOGGING);
        Logger.logString(baseLogName + "pwmName", pattern.toString(), Constants.ENABLE_LOGGING);
    }

    public BlinkinPattern getPattern() {
        return pattern;
    }
    public void setPatternLater(double delay, BlinkinPattern pattern){
        delayedLightEvents.put(delay,pattern);
    }

    public void scheduleOnTrue(Callable<Boolean> callable, BlinkinPattern pattern) {;
        predicateLightEvents.put(callable, pattern);
    }
}
