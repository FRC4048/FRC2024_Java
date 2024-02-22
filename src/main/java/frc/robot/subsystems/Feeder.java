package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.utils.ColorObject;
import frc.robot.constants.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

import java.util.Arrays;

public class Feeder extends SubsystemBase{

    private final WPI_TalonSRX feederMotor;
    private final I2C.Port i2cPort = I2C.Port.kMXP;
    private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch colorMatcher = new ColorMatch();

    public Feeder() {
        this.feederMotor = new WPI_TalonSRX(Constants.FEEDER_MOTOR_ID);
        this.feederMotor.setNeutralMode(NeutralMode.Brake);
        Arrays.stream(ColorObject.values()).forEach(c -> colorMatcher.addColorMatch(c.getColor()));
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
    public Color getColor() {
        return colorSensor.getColor();
    }
    public boolean pieceSeen() {
        return (getPiece() == ColorObject.Piece);
    }

    public boolean pieceNotSeen() {
        return (getPiece() == ColorObject.Plastic);
    }

    @Override
    public void periodic() {
        if (Constants.FEEDER_DEBUG) {
            Color detectedColor = colorSensor.getColor();
            double IR = colorSensor.getIR();
            double proximity = colorSensor.getProximity();
            ColorMatchResult matchedColor = colorMatcher.matchClosestColor(detectedColor);
            SmartShuffleboard.put("Feeder", "Feeder Motor Speed", getFeederMotorSpeed());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Red", detectedColor.red);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Green", detectedColor.green);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Blue", detectedColor.blue);
            SmartShuffleboard.put("Feeder", "Color Sensor",  "Infrared", IR);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Proximity", proximity);
            SmartShuffleboard.put("Feeder", "Color Sensor", "ObjectSeen", getPiece() == null ? "null" : getPiece().getName());
            SmartShuffleboard.put("Diagnostics", "Color Sensor", "ObjectSeen", getPiece() == null ? "null" : getPiece().getName());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Certainty", matchedColor.confidence);
        }
    }

    public ColorObject getPiece() {
        Color detectedColor = colorSensor.getColor();
        double IR = colorSensor.getIR();
        double proximity = colorSensor.getProximity();
        ColorMatchResult matchedColor = colorMatcher.matchClosestColor(detectedColor);
        return ColorObject.getFromColor(matchedColor.color);
    }
}