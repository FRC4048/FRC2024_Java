package frc.robot.commands.lightstrip;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LightStrip;

public class CycleLEDPattern extends Command {
    private final LightStrip strip;
    private final double patternDuration;
    private final Timer timer;

    public CycleLEDPattern(LightStrip strip, double patternDuration) {
        this.strip = strip;
        this.patternDuration = patternDuration;
        this.timer = new Timer();
    }

    @Override
    public void initialize() {
        timer.restart();
    }

    @Override
    public void execute() {
        if (timer.hasElapsed(patternDuration)){
            strip.setPattern(strip.getPattern().next());
            timer.restart();
        }
    }

    @Override
    public void end(boolean interrupted) {
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
