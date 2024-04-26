package frc.robot.subsystems.lightstrip;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.BlinkinPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class LightStrip extends SubsystemBase {
    private final LightstripIO lightstripIO;
    private final LightStripInputs input = new LightStripInputs();
    private final Map<BooleanSupplier, BlinkinPattern> predicateLightEvents = new HashMap<>();

    public LightStrip() {
        this.lightstripIO = new RealLightStrip();
    }

    @Override
    public void periodic() {
        lightstripIO.updateInputs(input);
        org.littletonrobotics.junction.Logger.processInputs("lightStripInput", input);
        predicateLightEvents.keySet()
                .stream()
                .filter(BooleanSupplier::getAsBoolean)
                .findFirst()
                .ifPresent(c -> {
                    setPattern(predicateLightEvents.get(c));
                    predicateLightEvents.remove(c);
                }
        );
    }

    public void setPattern(BlinkinPattern pattern) {
        lightstripIO.setPattern(pattern);
    }

    public BlinkinPattern getPattern() {
        return input.pattern;
    }

    public void scheduleOnTrue(BooleanSupplier callable, BlinkinPattern pattern) {
        predicateLightEvents.put(callable, pattern);
    }
}
