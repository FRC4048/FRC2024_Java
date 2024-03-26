package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagSparkMaxLimit;
import frc.robot.utils.logging.Logger;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;

public class Climber extends SubsystemBase {
    private final String baseLogName = "/robot/climber/";
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
        this.leftServo = new Servo(Constants.LEFT_SERVO_ID);
        this.rightServo = new Servo(Constants.RIGHT_SERVO_ID);
        configureMotors();

        rightExtendedLimit = climberRight.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        leftExtendedLimit = climberLeft.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);

        rightRetractedLimit = climberRight.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
        leftRetractedLimit = climberLeft.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);


        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(leftExtendedLimit, "Climber", "Left Extended"));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(rightExtendedLimit, "Climber", "Right Extended"));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(leftRetractedLimit, "Climber", "Left Retracted"));
        Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(rightRetractedLimit, "Climber", "Right Retracted"));
    }

    private void configureMotors() {
        this.climberLeft.restoreFactoryDefaults();
        this.climberRight.restoreFactoryDefaults();

        this.climberLeft.setIdleMode(IdleMode.kBrake);
        this.climberRight.setIdleMode(IdleMode.kBrake);
        this.climberLeft.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus0,20);
        this.climberLeft.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus1,100);
        this.climberLeft.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2,100);
        this.climberRight.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus0,20);
        this.climberRight.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus1,100);
        this.climberRight.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2,100);

        this.leftServo.setBoundsMicroseconds(2200, 0, 1500, 0, 800);
        this.rightServo.setBoundsMicroseconds(2200, 0, 1500, 0, 1100);
    }

    /**
     * Right motor - Positive is down, Negative is up
     * Left motor - Positive is up, Negative is down
     * @return true if setting speed was successful
     */
    public boolean setSpeed(double spd) {
        if (spd > 0 && ratchetEngaged) return false;
        if (spd > 0) {
            if (climberRight.getEncoder().getPosition() < -78) {
                climberRight.set(0);
            } else {
                climberRight.set(-spd);
            }
            if (climberLeft.getEncoder().getPosition() > 78) {
                climberLeft.set(0);
            } else {
                climberLeft.set(spd);
            }
        } else {
            climberRight.set(-spd);
            climberLeft.set(spd);
        }
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
    public boolean isLeftReverseLimitSwitchPressed() {
        return leftRetractedLimit.isPressed();
    }
    public boolean isRightReverseLimitSwitchPressed() {
        return rightRetractedLimit.isPressed();
    }
    @Override
    public void periodic() {
        if (Constants.CLIMBER_DEBUG) {
            SmartShuffleboard.put("Climber", "Left Retracted", leftRetractedLimit.isPressed());
            SmartShuffleboard.put("Climber", "Right Retracted", rightRetractedLimit.isPressed());
            SmartShuffleboard.put("Climber", "Left Extended", leftExtendedLimit.isPressed());
            SmartShuffleboard.put("Climber", "Right Extended", rightExtendedLimit.isPressed());

            SmartShuffleboard.put("Climber", "Left Encoder", climberLeft.getEncoder().getPosition());
            SmartShuffleboard.put("Climber", "Right Encoder", climberRight.getEncoder().getPosition());
        }
        Logger.logBoolean(baseLogName + "climber/engaged",ratchetEngaged, Constants.ENABLE_LOGGING);
    }
}
