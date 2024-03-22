package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.ColorSensor;
import frc.robot.utils.ColorValue;
import frc.robot.utils.diag.DiagColorSensor;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Feeder extends SubsystemBase {
    private final String baseLogName = "/robot/feeder/";
    private final WPI_TalonSRX feederMotor;
    private final I2C.Port i2cPort = I2C.Port.kMXP;
    private final ColorSensor colorSensor;

    public Feeder() {
        this.feederMotor = new WPI_TalonSRX(Constants.FEEDER_MOTOR_ID);
        this.feederMotor.setNeutralMode(NeutralMode.Brake);
        this.feederMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        colorSensor = new ColorSensor(i2cPort);
        Robot.getDiagnostics().addDiagnosable(new DiagColorSensor("Feeder", "Color Sensor", colorSensor));
    }

    public void setFeederMotorSpeed(double speed) {
        feederMotor.set(speed);
    }

    public double getFeederMotorSpeed() {
        return feederMotor.get();
    }

    public void stopFeederMotor() {
        feederMotor.set(0);
    }

    /**
     * @return returns true if feeder sensor is connected to digital IO
     */
    public ColorValue getColor() {
        return colorSensor.getColor();
    }

    public boolean pieceSeen(boolean incoming) {
        if (Constants.RELY_COLOR_SENSOR){
            ColorMatchResult latestResult = colorSensor.getMatchedColor();
            double confidence = incoming ? Constants.COLOR_CONFIDENCE_RATE_INCOMING : Constants.COLOR_CONFIDENCE_RATE_BACKDRIVE;
            return latestResult.confidence > confidence;
        }
        return this.getForwardSwitchTripped();
    }

    @Override
    public void periodic() {
        if (Constants.FEEDER_DEBUG) {
            ColorValue detectedColor = colorSensor.getColor();
            Color rawColor = colorSensor.getRawColor();
            ColorMatchResult matchedColor = colorSensor.getMatchedColor();
            SmartShuffleboard.put("Feeder", "Feeder Motor Speed", getFeederMotorSpeed());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Matched", detectedColor.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Red", rawColor.red);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Green", rawColor.green);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Blue", rawColor.blue);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Certainty", matchedColor.confidence);
            SmartShuffleboard.put("Feeder", "Piece Seen Incoming", pieceSeen(true));
            SmartShuffleboard.put("Feeder", "Piece Seen Reverse", pieceSeen(false));
            SmartShuffleboard.put("Driver", "Has Game Piece?", pieceSeen(false))
                    .withPosition(0, 0)
                    .withSize(2, 2);
        }

        Logger.logDouble(baseLogName + "FeederMotorSpeed",getFeederMotorSpeed(),Constants.ENABLE_LOGGING);
    }

    public boolean getForwardSwitchTripped() {
        return feederMotor.isFwdLimitSwitchClosed() == 1;
    }
    public void switchFeederBeamState(boolean enable) {
        if (enable){
            this.feederMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        }else {
            this.feederMotor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.Disabled);
        }
    }
}