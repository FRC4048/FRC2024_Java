package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.setLED;
import frc.robot.constants.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class LEDDisplay extends SubsystemBase{
    
    private Spark ledController = new Spark(Constants.LED_PWM_ID);

    public LEDDisplay() {
        if (Constants.LED_DEBUG) {
            SmartShuffleboard.put("LED", "PWMValue", 0.0);
            SmartShuffleboard.putCommand("LED", "Update Value", new setLED(this));
        }
    }

    @Override
    public void periodic() {
    }

    public void setValue(double newValue) {
        ledController.set(newValue);
    }
}
