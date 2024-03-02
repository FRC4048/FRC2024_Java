package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagSparkMaxLimit;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Climber extends SubsystemBase {
    private final CANSparkMax climberLeft;
    private final CANSparkMax climberRight; //invert this motor
    private final Servo leftServo;
    private final Servo rightServo;
    private final SparkLimitSwitch rightRetractedLimit;
    private final SparkLimitSwitch leftRetractedLimit;
    private final SparkLimitSwitch rightExtendedLimit;
    private final SparkLimitSwitch leftExtendedLimit;
    private boolean ratchetEngaged = true;

    public Climber() {
        this.climberLeft = new CANSparkMax(Constants.CLIMBER_LEFT, CANSparkMax.MotorType.kBrushless);
        this.climberRight = new CANSparkMax(Constants.CLIMBER_RIGHT, CANSparkMax.MotorType.kBrushless);

        this.climberLeft.restoreFactoryDefaults();
        this.climberRight.restoreFactoryDefaults();

        this.climberLeft.setIdleMode(IdleMode.kBrake);
        this.climberRight.setIdleMode(IdleMode.kBrake);

        rightExtendedLimit = climberRight.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        leftExtendedLimit = climberLeft.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);

        rightRetractedLimit = climberRight.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        leftRetractedLimit = climberLeft.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        
        // 800 - 2200
        this.leftServo = new Servo(Constants.LEFT_SERVO_ID);
        this.rightServo = new Servo(Constants.RIGHT_SERVO_ID);
        this.leftServo.setBoundsMicroseconds(2200, 0, 1500, 0, 800);
        this.rightServo.setBoundsMicroseconds(2200, 0, 1500, 0, 1100);

        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(leftExtendedLimit, "Climber", "Left Extended"));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(rightExtendedLimit, "Climber", "Right Extended"));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(leftRetractedLimit, "Climber", "Left Retracted"));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(rightRetractedLimit, "Climber", "Right Retracted"));
    }

    /**
     * Right motor - Positive is down, Negative is up
     * Left motor - Positive is up, Negative is down
     * @return true if setting speed was successful
     */
    public boolean setSpeed(double spd) {
        if (spd > 0 && ratchetEngaged) return false;
        climberRight.set(-spd);
        climberLeft.set(spd);
        return true;
    }

    public void resetEncoders() {
        this.climberLeft.getEncoder().setPosition(0);
        this.climberRight.getEncoder().setPosition(0);
    }    

    public void engageRatchet() {
        leftServo.setPosition(0);
        rightServo.setPosition(180);
        ratchetEngaged = true;
    }

    public void disengageRatchet() {
        leftServo.setPosition(180);
        rightServo.setPosition(0);
        ratchetEngaged = false;
    }
    public double getEncoderPosition() {
        return climberLeft.getEncoder().getPosition();
    }
    public boolean isExtendedLimitSwitchNotPressed() {
        return !(leftExtendedLimit.isPressed() || rightExtendedLimit.isPressed());
    }

    @Override
    public void periodic() {
        if (Constants.CLIMBER_DEBUG) {
            SmartShuffleboard.put("Climber", "Left Retracted", leftRetractedLimit.isPressed());
            SmartShuffleboard.put("Climber", "Right Retracted", rightRetractedLimit.isPressed());
            SmartShuffleboard.put("Climber", "Left Extended", leftExtendedLimit.isPressed());
            SmartShuffleboard.put("Climber", "Right Extended", rightExtendedLimit.isPressed());
        }
    }
}
