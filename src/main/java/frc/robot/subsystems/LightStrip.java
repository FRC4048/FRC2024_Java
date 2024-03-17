package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.logging.Logger;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class LightStrip extends SubsystemBase {
    private static final String baseLogName = "/robot/lightstrip/";
    private final Spark colorSensorPort;
    private BlinkinPattern pattern = BlinkinPattern.WHITE;
    private Map<Double, BlinkinPattern> lightEvents = new TreeMap<>(Double::compareTo);

    public LightStrip(int port) {
        this.colorSensorPort = new Spark(port);
    }

    @Override
    public void periodic() {
        lightEvents.keySet().stream()
                .filter(t -> t - Timer.getFPGATimestamp() <= TimeUnit.MILLISECONDS.convert(20,TimeUnit.SECONDS))
                .findFirst()
                .ifPresentOrElse(t -> setPattern(lightEvents.get(t)), ()->{});
    }

    public void setPattern(BlinkinPattern pattern) {
        this.pattern = pattern;
        colorSensorPort.set(pattern.getPwm());
        Logger.logDouble(baseLogName + "pwmSignal", pattern.getPwm(), Constants.ENABLE_LOGGING);
        Logger.logString(baseLogName + "pwmName", pattern.toString(), Constants.ENABLE_LOGGING);
    }

    public BlinkinPattern getPattern() {
        return pattern;
    }
    public void setPatternLater(double delay, BlinkinPattern pattern){
        lightEvents.put(delay,pattern);

    }
}
