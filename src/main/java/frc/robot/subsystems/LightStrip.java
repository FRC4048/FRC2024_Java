package frc.robot.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.logging.Logger;

public class LightStrip extends SubsystemBase {
    private static final String baseLogName = "/robot/lightstrip/";

    private final Spark colorSensorPort;
    private BlinkinPattern pattern = BlinkinPattern.WHITE;

    public LightStrip(int port) {
        this.colorSensorPort = new Spark(port);
    }

    @Override
    public void periodic() {
        Logger.logDouble(baseLogName + "pwmSignal", pattern.getPwm(), Constants.ENABLE_LOGGING);
        Logger.logString(baseLogName + "pwmName", pattern.toString(), Constants.ENABLE_LOGGING);
    }

    public void setPattern(BlinkinPattern pattern){
        this.pattern = pattern;
        colorSensorPort.set(pattern.getPwm());
    }

    public BlinkinPattern getPattern() {
        return pattern;
    }
}
