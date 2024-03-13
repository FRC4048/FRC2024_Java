package frc.robot.subsystems;

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
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Feeder extends SubsystemBase {

    private final WPI_TalonSRX feederMotor;
    private final I2C.Port i2cPort = I2C.Port.kMXP;
    private final ColorSensor colorSensor;
    private final AtomicInteger maxConfidence = new AtomicInteger();
    private final AtomicBoolean forceStopped = new AtomicBoolean(false);
    private final AtomicBoolean listenForceStop = new AtomicBoolean(false);
    private int thisTicMaxConfidence;

    public Feeder() {
        this.feederMotor = new WPI_TalonSRX(Constants.FEEDER_MOTOR_ID);
        this.feederMotor.setNeutralMode(NeutralMode.Brake);
        colorSensor = new ColorSensor(i2cPort);
        Robot.getDiagnostics().addDiagnosable(new DiagColorSensor("Feeder", "Color Sensor", colorSensor));
    }

    public synchronized void setFeederMotorSpeed(double speed) {
        feederMotor.set(speed);
    }

    public double getFeederMotorSpeed() {
        return feederMotor.get();
    }

    public void stopFeederMotor() {
        setFeederMotorSpeed(0);
    }

    /**
     * @return returns true if feeder sensor is connected to digital IO
     */
    public ColorValue getColor() {
        return colorSensor.getColor();
    }

    public boolean pieceSeen(boolean incoming) {
        return shouldStop(incoming, (double) thisTicMaxConfidence /100);
    }
    public static boolean shouldStop(boolean incoming, double sensorConfidence){
        double confidence = incoming ? Constants.COLOR_CONFIDENCE_RATE_INCOMING : Constants.COLOR_CONFIDENCE_RATE_BACKDRIVE;
        return sensorConfidence > confidence;
    }

    @Override
    public void periodic() {
        thisTicMaxConfidence = maxConfidence.get();
        maxConfidence.set(0);
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
        }

        SmartShuffleboard.put("Driver", "Has Game Piece?", pieceSeen(false))
            .withPosition(0, 0)
            .withSize(2, 2);
    }

    public boolean forceStopped() {
        return forceStopped.get();
    }
    public void setListenForceStop(boolean listen){
        this.listenForceStop.set(listen);
    }

    public ColorMatchResult getMatchedColor() {
        return colorSensor.getMatchedColor();
    }

    public double getMaxConfidence() {
        return maxConfidence.get();
    }

    /**
     * @param conf percent confidence: 0-100
     */
    private void setMaxConfidence(int conf) {
        this.maxConfidence.set(conf);
    }

    public boolean isListeningForceStop() {
        return listenForceStop.get();
    }

    public void setListeningForceStop(boolean shouldListen) {
        listenForceStop.set(shouldListen);
    }

    /**
     * @return if confidence is greater than the previous confidenceValue
     */
    public boolean updateColorSensor() {
        ColorMatchResult matchedColor = getMatchedColor();
        if (matchedColor.confidence > getMaxConfidence()) {
            setMaxConfidence((int) (matchedColor.confidence * 100));
            return true;
        }
        return false;
    }
}