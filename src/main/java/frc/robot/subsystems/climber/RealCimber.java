package frc.robot.subsystems.climber;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkLimitSwitch;
import edu.wpi.first.wpilibj.Servo;
import frc.robot.Robot;
import frc.robot.constants.Constants;
import frc.robot.utils.diag.DiagSparkMaxLimit;

public class RealCimber implements ClimberIO {
    private final CANSparkMax climberLeft;
    private final CANSparkMax climberRight; //invert this motor
    private final Servo leftServo;
    private final Servo rightServo;
    private final SparkLimitSwitch rightRetractedLimit;
    private final SparkLimitSwitch leftRetractedLimit;
    private final SparkLimitSwitch rightExtendedLimit;
    private final SparkLimitSwitch leftExtendedLimit;

    public RealCimber() {
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

        this.climberLeft.setIdleMode(CANSparkBase.IdleMode.kBrake);
        this.climberRight.setIdleMode(CANSparkBase.IdleMode.kBrake);
        this.climberLeft.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus0,20);
        this.climberLeft.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus1,100);
        this.climberLeft.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2,100);
        this.climberRight.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus0,20);
        this.climberRight.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus1,100);
        this.climberRight.setPeriodicFramePeriod(CANSparkLowLevel.PeriodicFrame.kStatus2,100);

        this.leftServo.setBoundsMicroseconds(2200, 0, 1500, 0, 800);
        this.rightServo.setBoundsMicroseconds(2200, 0, 1500, 0, 1100);
    }

    @Override
    public void setLeftSpeed(double spd) {
        climberLeft.set(spd);
    }

    @Override
    public void setRightSpeed(double spd) {
        climberRight.set(spd);
    }

    @Override
    public void resetEncoder() {
        climberLeft.getEncoder().setPosition(0);
        climberRight.getEncoder().setPosition(0);
    }

    @Override
    public void engageRatchet() {
        leftServo.set(Constants.LEFT_SERVO_ENGAGED);
        rightServo.set(Constants.RIGHT_SERVO_ENGAGED);
    }

    @Override
    public void disengageRatchet() {
        leftServo.set(Constants.LEFT_SERVO_DISENGAGED);
        rightServo.set(Constants.RIGHT_SERVO_DISENGAGED);
    }

    @Override
    public void updateInputs(ClimberInputs inputs) {
        inputs.leftServoSetpoint = leftServo.get();
        inputs.rightServoSetpoint = rightServo.get();
        inputs.atLeftClimberLimit = leftRetractedLimit.isPressed();
        inputs.atRightClimberLimit = rightRetractedLimit.isPressed();
        inputs.leftClimberEnc = climberLeft.getEncoder().getPosition();
        inputs.rightClimberEnc = climberRight.getEncoder().getPosition();
    }
}
