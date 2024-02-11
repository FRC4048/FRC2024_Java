package frc.robot.subsystems;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class ColorSensor extends SubsystemBase{
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    String colorObject;
    public ColorSensor() {
        m_colorMatcher.addColorMatch(Constants.K_PIECE_TARGET);
        m_colorMatcher.addColorMatch(Constants.K_PLASTIC_TARGET);
    }
    @Override
    public void periodic() {
        Color detectedColor = m_colorSensor.getColor();
        double IR = m_colorSensor.getIR();
        SmartShuffleboard.put("Feeder","Red", detectedColor.red);
        SmartShuffleboard.put("Feeder","Green", detectedColor.green);
        SmartShuffleboard.put("Feeder","Blue", detectedColor.blue);
        SmartShuffleboard.put("Feeder","Infrared", IR);
        double proximity = m_colorSensor.getProximity();
        SmartShuffleboard.put("Feeder","Proximity", proximity);
        ColorMatchResult matchedColor = m_colorMatcher.matchClosestColor(detectedColor);
        if (matchedColor.color == Constants.K_PIECE_TARGET) {
            colorObject="Piece";
        } else if (matchedColor.color == Constants.K_PLASTIC_TARGET) {
            colorObject="Plastic";
        } else {
            colorObject="None";
        }
        
        SmartShuffleboard.put("Feeder","ObjectColor", colorObject);
        SmartShuffleboard.put("Feeder","Certainty", matchedColor.confidence);
    }
    public Color getColor() {
        return m_colorSensor.getColor();
    }
    public boolean pieceSeen() {
        if (colorObject=="Piece") {
            return true;
        } else {
            return false;
        }
    }
}
