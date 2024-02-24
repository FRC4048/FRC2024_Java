package frc.robot.subsystems;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkLimitSwitch;
import com.revrobotics.SparkLimitSwitch.Type;
import com.revrobotics.SparkPIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.NeoPidMotor;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;


public class Ramp extends SubsystemBase {
    private final CANSparkMax neoMotor;
    private final RelativeEncoder encoder;
    private final NeoPidMotor neoPidMotor;
    private double pidP = Constants.RAMP_PID_P;
    private double pidI = Constants.RAMP_PID_I;
    private double pidD = Constants.RAMP_PID_D;
    private double pidFF = Constants.RAMP_PID_FF;
    private double rampPos = Constants.RAMP_POS;
    private double iZoneError = Constants.RAMP_ERROR_IZONE;
    private final SparkLimitSwitch forwardLimitSwitch;
    private final SparkLimitSwitch backwardLimitSwitch;

    public Ramp() {
        neoPidMotor = new NeoPidMotor(Constants.RAMP_ID);
        neoPidMotor.setPid(pidP, pidI, pidD, iZoneError, pidFF, -1, 1);
        neoPidMotor.setSmartMotionVelocity(Constants.RAMP_MAX_RPM_VELOCITY, 0.0, 0);
        neoPidMotor.setSmartMotionMaxAccel(Constants.RAMP_MAX_RPM_ACCELERATION, 0);	
        neoPidMotor.setSmartMotionAllowedClosedLoopError(0.0, 0);
        neoMotor = neoPidMotor.getNeoMotor();
        encoder = neoMotor.getEncoder();
        resetEncoder();
        forwardLimitSwitch = neoMotor.getForwardLimitSwitch(Type.kNormallyOpen);
        backwardLimitSwitch = neoMotor.getReverseLimitSwitch(Type.kNormallyOpen);
        if (Constants.RAMP_PID_DEBUG){
//            SmartShuffleboard.put("Ramp", "PID P", pidP);
//            SmartShuffleboard.put("Ramp", "PID I", pidI);
//            SmartShuffleboard.put("Ramp", "PID D", pidD);
        }
    }

    public void periodic() {
        if (Constants.RAMP_DEBUG){
            SmartShuffleboard.put("Ramp", "P Gain", pidP);
            SmartShuffleboard.put("Ramp", "I Gain", pidI);
            SmartShuffleboard.put("Ramp", "D Gain", pidD);
            SmartShuffleboard.put("Ramp", "FF Gain", pidFF);
            SmartShuffleboard.put("Ramp", "Encoder Value", getRampPos());
            SmartShuffleboard.put("Ramp", "Desired pos", rampPos);
            SmartShuffleboard.put("Ramp", "Reverse Switch Tripped", getReversedSwitchState());
            SmartShuffleboard.put("Ramp", "Forward Switch Tripped", getForwardSwitchState());
        }
        if (Constants.RAMP_PID_DEBUG){
            //            // pid tuning
            pidP = SmartShuffleboard.getDouble("Ramp", "PID P", pidP);
            pidI = SmartShuffleboard.getDouble("Ramp", "PID I", pidI);
            pidD = SmartShuffleboard.getDouble("Ramp", "PID D", pidD);
            pidFF = SmartShuffleboard.getDouble("Ramp", "PID FF", pidFF);
        }

    }

    public void setRampPos(double targetPosition) {
        neoPidMotor.setPidPos(targetPosition);
        this.rampPos = targetPosition;
    }
    
    /**
     * This Java Docs is bad
     * Sets the motor speed
     * Check if forward had a positive or negative speed value
     * @param speed the speed (0-1)
     */
    public void setMotor(double speed) {
        neoMotor.set(speed);
    }

    public void stopMotor() {
        neoMotor.set(0.0);
    }

    public double getRampPos() {
        return encoder.getPosition();
    }

    /**
     * @return If the Forward Limit Switch is pressed
     */
    public boolean getForwardSwitchState() {
        return forwardLimitSwitch.isPressed();
    }

    /**
     * @return If the Reversed Limit Switch is pressed
     */
    public boolean getReversedSwitchState() {
        return backwardLimitSwitch.isPressed();
    }

    public void resetEncoder() {
        this.rampPos = 0;
        encoder.setPosition(0);
    }

    public void setPID() {
        neoPidMotor.setPid(pidP, pidI, pidD, iZoneError, pidFF, -1, 1);
    }
    public void setSpeed(double spd){
        neoMotor.set(spd);
    }
    public double encoderToAngle(double encoderValue){
        //y=mx+b
        return 2.48 * encoderValue + 28.5;//needs be to measured again and put in constants
    }
}