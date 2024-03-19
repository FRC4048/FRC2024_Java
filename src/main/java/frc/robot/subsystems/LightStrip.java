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
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class LightStrip extends SubsystemBase {
    private static final String baseLogName = "/robot/lightstrip/";
    private final Spark colorSensorPort;
    private BlinkinPattern pattern = BlinkinPattern.WHITE;
    private final Map<BooleanSupplier, BlinkinPattern> predicateLightEvents = new HashMap<>();

    public LightStrip(int port) {
        this.colorSensorPort = new Spark(port);
    }

    @Override
    public void periodic() {
        predicateLightEvents.keySet()
                .stream()
                .filter(BooleanSupplier::getAsBoolean)
                .findFirst()
                .ifPresentOrElse(c -> {
                    setPattern(predicateLightEvents.get(c));
                    predicateLightEvents.remove(c);
                }, ()->{}
        );
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
    public void scheduleOnTrue(BooleanSupplier callable, BlinkinPattern pattern) {;
        predicateLightEvents.put(callable, pattern);
    }
    public static BooleanSupplier isDelayOver(double delay){
        double startTime = Timer.getFPGATimestamp();
        return () -> hasHappned(startTime + delay);
    }

    private static boolean hasHappned(double time) {
        return time - Timer.getFPGATimestamp() <= TimeUnit.MILLISECONDS.convert(20,TimeUnit.SECONDS);
    }
}
