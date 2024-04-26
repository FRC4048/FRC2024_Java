package frc.robot.subsystems.feeder;

import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;

public class Feeder extends SubsystemBase {
    private final FeederIO feederIO;
    private final FeederInputs inputs = new FeederInputs();

    public Feeder(FeederIO io) {
        this.feederIO = io;
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
            ColorMatchResult latestResult = inputs.colorMatchResult;
            double confidence = incoming ? Constants.COLOR_CONFIDENCE_RATE_INCOMING : Constants.COLOR_CONFIDENCE_RATE_BACKDRIVE;
            return latestResult.confidence > confidence;
        }
        return inputs.isFwdTripped;
    }

    @Override
    public void periodic() {
        feederIO.updateInputs(inputs);
        org.littletonrobotics.junction.Logger.processInputs("feederInputs", inputs);
    }

    public void switchFeederBeamState(boolean enable) {
        feederIO.switchFeederBeamState(enable);
    }
}