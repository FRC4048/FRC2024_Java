package frc.robot.commands.lightstrip;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LightStrip;
import frc.robot.utils.BlinkinPattern;

public class SetLEDPattern extends Command {
    private final LightStrip led;
    private final BlinkinPattern pattern;

    public SetLEDPattern(LightStrip led, BlinkinPattern pattern) {
        this.led = led;
        this.pattern = pattern;
        addRequirements(led);
    }

    @Override
    public void execute() {
        led.setPattern(pattern);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
