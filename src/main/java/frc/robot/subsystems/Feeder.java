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
    private final I2C.Port i2cPort = I2C.Port.kMXP;
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
    public ColorValue getColor25() {
        return colorSensor.getColor25();
    }
    public ColorValue getColor10() {
        return colorSensor.getColor10();
    }
    public ColorValue getColor5() {
        return colorSensor.getColor5();
    }

    public boolean pieceSeen50() {
        return (getColor50() == ColorValue.Piece50);
    }
    public boolean pieceSeen25() {
        return (getColor25() == ColorValue.Piece25);
    }
    public boolean pieceSeen10() {
        return (getColor10() == ColorValue.Piece10);
    }
    public boolean pieceSeen5() {
        return (getColor5() == ColorValue.Piece5);
    }

    public boolean pieceNotSeen50() {
        return (getColor50() == ColorValue.Plastic);
    }
    public boolean pieceNotSeen25() {
        return (getColor25() == ColorValue.Plastic);
    }
    public boolean pieceNotSeen10() {
        return (getColor10() == ColorValue.Plastic);
    }
    public boolean pieceNotSeen5() {
        return (getColor5() == ColorValue.Plastic);
    }

    @Override
    public void periodic() {
        if (Constants.FEEDER_DEBUG) {
            ColorValue detectedColor50 = colorSensor.getColor50();
            ColorValue detectedColor25 = colorSensor.getColor25();
            ColorValue detectedColor10 = colorSensor.getColor10();
            ColorValue detectedColor5 = colorSensor.getColor5();
            double certainty50 = colorSensor.getMatchCertainity50();
            double certainty25 = colorSensor.getMatchCertainity25();
            double certainty10 = colorSensor.getMatchCertainity10();
            double certainty5 = colorSensor.getMatchCertainity5();
            Color rawColor = colorSensor.getRawColor();
            SmartShuffleboard.put("Feeder", "Feeder Motor Speed", getFeederMotorSpeed());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Matched50", detectedColor50.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Matched25", detectedColor25.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Matched10", detectedColor10.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Matched5", detectedColor5.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Red", rawColor.red);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Green", rawColor.green);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Blue", rawColor.blue);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Certainty50", certainty50);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Certainty25", certainty25);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Certainty10", certainty10);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Certainty5", certainty5);
        }
    }
}