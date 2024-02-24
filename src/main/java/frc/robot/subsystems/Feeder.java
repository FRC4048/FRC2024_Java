package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatchResult;
import edu.wpi.first.wpilibj.I2C;
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
    public ColorValue getColor() {
        return colorSensor.getColor();
    }

    public boolean pieceSeen() {
        return (getColor() == ColorValue.Piece);
    }

    public boolean pieceNotSeen() {
        return (getColor() == ColorValue.Plastic);
    }

    @Override
    public void periodic() {
        if (Constants.FEEDER_DEBUG) {
            ColorValue detectedColor = colorSensor.getColor();
            ColorMatchResult rawColor = colorSensor.getRawColor();
            SmartShuffleboard.put("Feeder", "Feeder Motor Speed", getFeederMotorSpeed());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Matched", detectedColor.name());
            SmartShuffleboard.put("Feeder", "Color Sensor", "Red", rawColor.color.red);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Green", rawColor.color.green);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Blue", rawColor.color.blue);
            SmartShuffleboard.put("Feeder", "Color Sensor", "Certainty", rawColor.confidence);
        }
    }
}