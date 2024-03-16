package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDDisplay;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class setLED extends Command {
    private LEDDisplay display;

    public setLED(LEDDisplay display) {
        this.display = display;
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public void initialize() {
        double updatedValue = SmartShuffleboard.getDouble("LED", "PWMValue", 0.0);
        display.setValue(updatedValue);
    }

    @Override
    public boolean isFinished() {
        return true;
    }    
}
