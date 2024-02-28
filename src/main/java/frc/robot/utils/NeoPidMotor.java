package frc.robot.utils;

import com.revrobotics.*;
import com.revrobotics.CANSparkBase.IdleMode;

import frc.robot.Robot;
import frc.robot.utils.diag.DiagSparkMaxLimit;

/**
 * A Wrapper utility to encapsulate the NEO motor with PID capability.
 * This is simply a wrapper with some convenient defaults and initializations that make programming
 * the PID of the NEO easier.
 *
 * TODO: This does not yet support the external absolute encoder that may be needed
 * TODO: This does not yet support velocity PID or other advanced features
 */
public class NeoPidMotor {
    public static final double DEFAULT_P = 5e-5;
    public static final double DEFAULT_I = 1e-6;
    public static final double DEFAULT_D = 0.0;
    public static final double DEFAULT_IZONE = 0.0;
    public static final double DEFAULT_FF = 0.000156;
    public static final double DEFAULT_MIN_OUTPUT = -1;
    public static final double DEFAULT_MAX_OUTPUT = 1;

    // The neo motor controller
    private CANSparkMax neoMotor;
    // The built-in relative encoder
    private RelativeEncoder encoder;
    // The built-in PID controller
    private SparkMaxPIDController pidController;

    // The desired motor position
    private double setPosition = 0.0;
    private double setSpeed = 0.0;

    private SparkLimitSwitch forwardSwitch = null;
    private SparkLimitSwitch reverseSwitch = null;

    /**
     * Constructor using reasonable default values
     * @param id the CAN ID for the controller
     */
    public NeoPidMotor(int id) {
        this(id, DEFAULT_P, DEFAULT_I, DEFAULT_D, DEFAULT_IZONE, DEFAULT_FF, DEFAULT_MIN_OUTPUT, DEFAULT_MAX_OUTPUT);
    }

    public NeoPidMotor(int id, double pidP, double pidI, double pidD, double iZone, double pidFF, double pidMinOutput, double pidMaxOutput) {
        neoMotor = new CANSparkMax(id, CANSparkMaxLowLevel.MotorType.kBrushless);
        neoMotor.restoreFactoryDefaults();
        encoder = neoMotor.getEncoder();
        // This is normally how we want it, but we may need to configure the switches differently
        forwardSwitch = neoMotor.getForwardLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);
        reverseSwitch = neoMotor.getReverseLimitSwitch(SparkMaxLimitSwitch.Type.kNormallyOpen);

        pidController = neoMotor.getPIDController();
        pidController.setP(pidP);
        pidController.setI(pidI);
        pidController.setD(pidD);
        pidController.setIZone(iZone);
        pidController.setFF(pidFF);
        pidController.setOutputRange(pidMinOutput, pidMaxOutput);

        // TODO: Some of these values may need to be configurable
        pidController.setSmartMotionMaxVelocity(5000.0, 0);
        pidController.setSmartMotionMinOutputVelocity(-5000.0, 0);
        pidController.setSmartMotionMaxAccel(1500.0, 0);
        pidController.setSmartMotionAllowedClosedLoopError(1.0, 0);
    }

    public void enableDiagnostics (String subSystem, boolean forwardLimit, boolean reverseLimit) {
        if (forwardLimit) {
            Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(forwardSwitch, subSystem, "forward"));
        }

        if (reverseLimit) {
            Robot.getDiagnostics().addDiagnosable(new DiagSparkMaxLimit(reverseSwitch, subSystem, "reverse"));
        }
    }

    public void setMinMaxVelocity(double maxVelocity, double minVelocity) {
        pidController.setSmartMotionMaxVelocity(maxVelocity, 0);
        pidController.setSmartMotionMinOutputVelocity(minVelocity, 0);
    }

    public void setMaxAccel(double maxAcceleration) {
        pidController.setSmartMotionMaxAccel(maxAcceleration, 0);
    }

    public void setSmartMotionAllowedClosedLoopError(double allowedError) {
        pidController.setSmartMotionAllowedClosedLoopError(allowedError, 0);
    }

    /**
     * Set the desired position using the relative encoder as a reference
     * @param position the desired motor position
     */
    public void setPidPos(double position) {
        pidController.setReference(position, CANSparkMax.ControlType.kSmartMotion);
        this.setPosition = position;
    }

    public double getSetPosition() {
        return setPosition;
    }

    public double getCurrentPosition() {
        return encoder.getPosition();
    }

    /**
     * Set the desired position using the relative encoder as a reference
     * @param speed the desired motor position
     */
    public void setPidSpeed(double speed) {
        pidController.setReference(speed, CANSparkMax.ControlType.kSmartVelocity);
//        pidController.setReference(speed, CANSparkMax.ControlType.kSmartVelocity);
        this.setSpeed = speed;
    }

    public double getSetSpeed() {
        return setSpeed;
    }

    public double getCurrentSpeed() {
        return encoder.getVelocity();
    }

    public void setPid(double pidP, double pidI, double pidD) {
        pidController.setP(pidP);
        pidController.setI(pidI);
        pidController.setD(pidD);
    }

    public void setPid(double pidP, double pidI, double pidD, double iZone, double pidFF, double pidMinOutput, double pidMaxOutput) {
        pidController.setP(pidP);
        pidController.setI(pidI);
        pidController.setD(pidD);
        pidController.setIZone(iZone);
        pidController.setFF(pidFF);
        pidController.setOutputRange(pidMinOutput, pidMaxOutput);
    }

    public CANSparkMax getNeoMotor() {
        return neoMotor;
    }

    public RelativeEncoder getEncoder() {
        return encoder;
    }

    public SparkMaxPIDController getPidController() {
        return pidController;
    }
    
    public void resetEncoderPosition() {
        encoder.setPosition(0);
    }

    /**
     * Re-using declared limit switch so that implementations don't accidentally change
     * from normallyOpen to normallyClosed in the constructor but not in limitSwitchPressed
     */
    public boolean forwardLimitSwitchIsPressed() {
        return forwardSwitch.isPressed();
    }

    /**
     * Re-using declared limit switch so that implementations don't accidentally change
     * from normallyOpen to normallyClosed in the constructor but not in limitSwitchPressed
     */
    public boolean reversedLimitSwitchIsPressed() {
        return reverseSwitch.isPressed();
    }

    public void setInverted(boolean isInverted) {
        neoMotor.setInverted(isInverted);
    }

    public void setIdleMode(IdleMode mode) {
        neoMotor.setIdleMode(mode);
    }
}
