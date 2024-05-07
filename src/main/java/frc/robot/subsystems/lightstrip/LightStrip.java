package frc.robot.subsystems.lightstrip;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.LoggableSystem;
import frc.robot.utils.BlinkinPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BooleanSupplier;

public class LightStrip extends SubsystemBase {
    private final LoggableSystem<LightStripIO, LightStripInputs> system;
    private final Map<BooleanSupplier, BlinkinPattern> predicateLightEvents = new HashMap<>();

    public LightStrip(LightStripIO io) {
        this.system = new LoggableSystem<>(io, new LightStripInputs());
    }

    @Override
    public void periodic() {
        system.updateInputs();
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
        system.getIO().setPattern(pattern);
    }

    public BlinkinPattern getPattern() {
        return system.getInputs().pattern;
    }

    public void scheduleOnTrue(BooleanSupplier callable, BlinkinPattern pattern) {
        predicateLightEvents.put(callable, pattern);
    }
}
