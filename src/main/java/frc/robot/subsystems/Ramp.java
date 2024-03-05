package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.NeoPidMotor;
import frc.robot.utils.smartshuffleboard.SmartShuffleboard;


public class Ramp extends SubsystemBase {
    private final NeoPidMotor neoPidMotor;
    private double rampPos = Constants.RAMP_POS;
    private boolean shootClosePos;
    private boolean shootAwayPos;
    private boolean shootAmp;

    public Ramp() {
        neoPidMotor = new NeoPidMotor(Constants.RAMP_ID);
        neoPidMotor.setSmartMotionAllowedClosedLoopError(Constants.RAMP_ERROR_RANGE);
        neoPidMotor.setMaxAccel(Constants.RAMP_MAX_RPM_ACCELERATION);
        resetEncoder();

        neoPidMotor.enableDiagnostics("Ramp", true, true);
    }

    public void periodic() {
        if (Constants.RAMP_DEBUG){
            SmartShuffleboard.put("Ramp", "Encoder Value", getRampPos());
            SmartShuffleboard.put("Ramp", "Desired pos", rampPos);
            SmartShuffleboard.put("Ramp", "Reverse Switch Tripped", getReversedSwitchState());
            SmartShuffleboard.put("Ramp", "Forward Switch Tripped", getForwardSwitchState());
        }

        SmartShuffleboard.put("Driver", "Speaker Close", isShootCloseAngle())
            .withPosition(10, 0)
            .withSize(1, 1);
        SmartShuffleboard.put("Driver", "Speaker Away", isShootAwayAngle())
            .withPosition(10, 1)
            .withSize(1, 1);
        SmartShuffleboard.put("Driver", "Amp", isShootAmpAngle())
            .withPosition(9, 1)
            .withSize(1, 1);
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
    public void setSpeed(double speed) {
        neoPidMotor.getNeoMotor().set(speed);
    }

    public void stopMotor() {
        neoPidMotor.getNeoMotor().set(0.0);
    }

    public double getRampPos() {
        return neoPidMotor.getEncoder().getPosition();
    }

    /**
     * @return If the Forward Limit Switch is pressed
     */
    public boolean getForwardSwitchState() {
        return neoPidMotor.forwardLimitSwitchIsPressed();
    }

    /**
     * @return If the Reversed Limit Switch is pressed
     */
    public boolean getReversedSwitchState() {
        return neoPidMotor.reversedLimitSwitchIsPressed();
    }

    public double getDesiredPosition() {
        return rampPos;
    }

    public void resetEncoder() {
        this.rampPos = 0;
        neoPidMotor.resetEncoderPosition();
    }

    public static double encoderToAngle(double encoderValue){
        //y=mx+b
        return 2.48 * encoderValue + 28.5;//needs be to measured again and put in constants
    }

    public boolean isShootCloseAngle(){
        return (Math.abs(Constants.RAMP_POS_SHOOT_SPEAKER_CLOSE - getRampPos()) <= Constants.RAMP_POS_THRESHOLD);
    }

    public boolean isShootAwayAngle(){
        return (Math.abs(Constants.RAMP_POS_SHOOT_SPEAKER_AWAY - getRampPos()) <= Constants.RAMP_POS_THRESHOLD);
    }

    public boolean isShootAmpAngle(){
        return (Math.abs(Constants.RAMP_POS_SHOOT_AMP - getRampPos()) <= Constants.RAMP_POS_THRESHOLD);
    }
    public static double angleToEncoder(double angle){
        //(y-b)/m=x
        return (angle - 28.5) / 2.48;//needs be to measured again and put in constants
    }

    public void setAngle(Rotation2d angleFromGround) {
        setRampPos(angleToEncoder(angleFromGround.getDegrees()));
    }
}