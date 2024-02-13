package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkLimitSwitch.Type;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;


public class Ramp extends SubsystemBase {
    private CANSparkMax neoMotor;
    private RelativeEncoder encoder;
    private SparkPIDController pidController;
    private double pidP = Constants.RAMP_PID_P;
    private double pidI = Constants.RAMP_PID_I;
    private double pidD = Constants.RAMP_PID_D;
    private double pidFF = Constants.RAMP_PID_FF;
    private double ramppos = Constants.RAMP_POS;
    private double iZoneError = Constants.RAMP_ERROR_IZONE;

    public Ramp() {
        neoMotor = new CANSparkMax(Constants.RAMP_ID, MotorType.kBrushless);
        neoMotor.restoreFactoryDefaults();
        encoder = neoMotor.getEncoder();
        resetEncoder();
        neoMotor.getForwardLimitSwitch(Type.kNormallyOpen);
        neoMotor.getReverseLimitSwitch(Type.kNormallyOpen);

        pidController = neoMotor.getPIDController();
        pidController.setP(pidP);
        pidController.setI(pidI);
        pidController.setD(pidD);
        pidController.setIZone(iZoneError);
        pidController.setFF(pidFF);
        pidController.setOutputRange(-1, 1);

        pidController.setSmartMotionMaxVelocity(500.0, 0);
        pidController.setSmartMotionMinOutputVelocity(0.0, 0);
        pidController.setSmartMotionMaxAccel(1500.0, 0);
        pidController.setSmartMotionAllowedClosedLoopError(0.0, 0);

        SmartShuffleboard.put("Ramp", "PID P", pidP);
        SmartShuffleboard.put("Ramp", "PID I", pidI);
        SmartShuffleboard.put("Ramp", "PID D", pidD);
    }

    public void periodic() {
        if (Constants.RAMP_DEBUG){
            SmartShuffleboard.put("Ramp", "P Gain", pidController.getP());
            SmartShuffleboard.put("Ramp", "I Gain", pidController.getI());
            SmartShuffleboard.put("Ramp", "D Gain", pidController.getD());
            SmartShuffleboard.put("Ramp", "FF Gain", pidController.getFF());
            SmartShuffleboard.put("Ramp", "Encoder Value", getRampPos());
            SmartShuffleboard.put("Ramp", "Desired pos", ramppos);
            // pid tuning
            pidP = SmartShuffleboard.getDouble("Ramp", "PID P", pidP);
            pidI = SmartShuffleboard.getDouble("Ramp", "PID I", pidI);
            pidD = SmartShuffleboard.getDouble("Ramp", "PID D", pidD);
            pidFF = SmartShuffleboard.getDouble("Ramp", "PID FF", pidFF);
        }

    }

    public void setRampPos(double rotations) {
        pidController.setReference(rotations, CANSparkMax.ControlType.kPosition);
        this.ramppos = rotations;
    }

    public double getRampPos() {
        return encoder.getPosition();
    }

    public boolean getForwardSwitch() {
        return neoMotor.getForwardLimitSwitch(Type.kNormallyOpen).isPressed();
    }

    public boolean getReversedSwitch() {
        return neoMotor.getReverseLimitSwitch(Type.kNormallyOpen).isPressed();
    }

    public void changeRampPos(double increment) {
        double newRampPos = Math.max(0.0, Math.min(40.0, this.ramppos + increment));
        setRampPos(newRampPos);
    }

    public void resetEncoder() {
        encoder.setPosition(0);
    }

    public void setPID() {
        pidController.setP(pidP);
        pidController.setI(pidI);
        pidController.setD(pidD);
        pidController.setFF(pidFF);
    }
}