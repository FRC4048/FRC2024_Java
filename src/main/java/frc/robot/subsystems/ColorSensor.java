package frc.robot.subsystems;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.GameConstants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class ColorSensor extends SubsystemBase{
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch m_colorMatcher = new ColorMatch();
    public enum ColorObject  {
            Piece,
            Plastic,
            None
        }
    private ColorObject piece = ColorObject.None;

    public ColorSensor() {
        m_colorMatcher.addColorMatch(GameConstants.C_PIECE_TARGET);
        m_colorMatcher.addColorMatch(GameConstants.C_PLASTIC_TARGET);
    }
    @Override
    public void periodic() {
        Color detectedColor = m_colorSensor.getColor();
        double IR = m_colorSensor.getIR();
        String colorObject = "";
        double proximity = m_colorSensor.getProximity();
        
        ColorMatchResult matchedColor = m_colorMatcher.matchClosestColor(detectedColor);
        if (matchedColor.color == GameConstants.C_PIECE_TARGET) piece  = ColorObject.Piece;
        else if (matchedColor.color == GameConstants.C_PLASTIC_TARGET) piece  = ColorObject.Plastic;
        else piece  = ColorObject.None;
        switch(piece) {
            case Piece:
              colorObject = "Piece";
              break;
            case Plastic:
               colorObject = "Plastic";
              break;
            case None:
              colorObject = "None";
              break;
          }
        if (GameConstants.COLOR_SENSOR_DEBUG){
            SmartShuffleboard.put("Feeder","Red", detectedColor.red);
            SmartShuffleboard.put("Feeder","Green", detectedColor.green);
            SmartShuffleboard.put("Feeder","Blue", detectedColor.blue);
            SmartShuffleboard.put("Feeder","Infrared", IR);
            SmartShuffleboard.put("Feeder","Proximity", proximity);
            SmartShuffleboard.put("Feeder","ObjectColor", colorObject);
            SmartShuffleboard.put("Feeder","Certainty", matchedColor.confidence);
        }
        
    }
    public Color getColor() {
        return m_colorSensor.getColor();
    }
    public boolean pieceSeen() {
        return (piece == ColorObject.Piece);
    }
    
}
