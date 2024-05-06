package frc.robot.subsystems.feeder;

import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.subsystems.colorsensor.ColorSensor;

public class Feeder extends SubsystemBase {
    private final FeederIO feederIO;
    private final ColorSensor colorSensor;
    private final FeederInputs inputs = new FeederInputs();

    public Feeder(FeederIO feederIO, ColorSensor colorSensor) {
        this.feederIO = feederIO;
        this.colorSensor = colorSensor;
    }

    public void setFeederMotorSpeed(double speed) {
        feederIO.setSpeed(speed);
    }

    public double getFeederMotorSpeed() {
        return inputs.feederSpeed;
    }

    public void stopFeederMotor() {
        feederIO.stop();
    }

    public boolean pieceSeen(boolean incoming) {
        if (Constants.RELY_COLOR_SENSOR){
            ColorMatchResult latestResult = colorSensor.getMatchedColor();
            double confidence = incoming ? Constants.COLOR_CONFIDENCE_RATE_INCOMING : Constants.COLOR_CONFIDENCE_RATE_BACKDRIVE;
            return latestResult.confidence > confidence;
        }
        return inputs.isFwdTripped;
    }

    @Override
    public void periodic() {
        feederIO.updateInputs(inputs);
        org.littletonrobotics.junction.Logger.processInputs("feederInputs", inputs);
        colorSensor.updateInputs();
    }

    public void switchFeederBeamState(boolean enable) {
        feederIO.switchFeederBeamState(enable);
    }
}