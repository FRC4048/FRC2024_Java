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

public class Feeder extends SubsystemBase {

    private final WPI_TalonSRX feederMotor;
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensor colorSensor;

    public Feeder() {
        this.feederMotor = new WPI_TalonSRX(Constants.FEEDER_MOTOR_ID);
        this.feederMotor.setNeutralMode(NeutralMode.Brake);

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
    public ColorValue getColor50() {
        return colorSensor.getColor50();
    }


    public boolean pieceSeen50() {
        return (getColor50() == ColorValue.Piece50);
    }

    public boolean pieceNotSeen50() {
        return (getColor50() == ColorValue.Plastic);
    }


    @Override
    public void periodic() {
        if (Constants.FEEDER_DEBUG) {
            ColorValue detectedColor50 = colorSensor.getColor50();
            ColorValue detectedColorPointPoint1 = colorSensor.getColorPointPoint1();
            ColorValue detectedColorConcoction = colorSensor.getColorConcoction();
            ColorValue detectedColorConfidence = colorSensor.getColorConfidence();
            double certainty50 = colorSensor.getMatchCertainty50();
            double certaintyPointPoint1 = colorSensor.getMatchCertaintyPointPoint1();
            double certaintyConcoction = colorSensor.getMatchCertaintyConcoction();
            Color rawColor = colorSensor.getRawColor();
            SmartShuffleboard.put("Feeder", "Feeder Motor Speed", getFeederMotorSpeed());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Matched50", detectedColor50.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "MatchedPointPoint1", detectedColorPointPoint1.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "MatchedConcoction", detectedColorConcoction.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "MatchedConfidence", detectedColorConfidence.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Red", rawColor.red);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Green", rawColor.green);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Blue", rawColor.blue);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Certainty50", certainty50);
            SmartShuffleboard.put("Feeder", "Color Sensor", "CertaintyPointPoint1", certaintyPointPoint1);
            SmartShuffleboard.put("Feeder", "Color Sensor", "CertaintyConcoction", certaintyConcoction);
        }
    }
}