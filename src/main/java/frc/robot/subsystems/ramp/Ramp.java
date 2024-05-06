package frc.robot.subsystems.ramp;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.utils.advanced.Alignable;
import frc.robot.utils.motor.NeoPidMotor;


public class Ramp extends SubsystemBase {
    private final RampIO rampIO;
    private final RampInputs inputs = new RampInputs();
    private final InterpolatingDoubleTreeMap rampAngleMap = new InterpolatingDoubleTreeMap();

    public Ramp(RampIO io) {
        rampIO = io;
        rampAngleMap.put(1.49352, 3.0);
        rampAngleMap.put(1.806956, 5.0);
        rampAngleMap.put(2.3876, 7.56);
        rampAngleMap.put(2.6162, 7.9);
    }

    public void periodic() {
        rampIO.updateInputs(inputs);
        org.littletonrobotics.junction.Logger.processInputs("rampInputs", inputs);
    }

    public void setRampPos(double targetPosition) {
        rampIO.setRampPos(targetPosition);
    }
    
    /**
     * This Java Docs is bad
     * Sets the motor speed
     * Check if forward had a positive or negative speed value
     * @param speed the speed (0-1)
     */
    public void setSpeed(double speed) {
        rampIO.setSpeed(speed);
    }

    public void stopMotor() {
        rampIO.stopMotor();
    }

    public double getRampPos() {
        return inputs.encoderPosition;
    }

    /**
     * @return If the Forward Limit Switch is pressed
     */
    public boolean getForwardSwitchState() {
        return inputs.fwdTripped;
    }

    /**
     * @return If the Reversed Limit Switch is pressed
     */
    public boolean getReversedSwitchState() {
        return inputs.revTripped;
    }

    public double getDesiredPosition() {
        return inputs.rampTargetPos;
    }

    public void resetEncoder() {
        rampIO.resetEncoder();
    }

    public static double encoderToAngle(double encoderValue){
        //y=mx+b
        return Constants.RAMP_ENCODER_TO_ANGLE_SLOPE * encoderValue + Constants.RAMP_ENCODER_TO_ANGLE_Y_INTERCEPT;
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
        return (angle - Constants.RAMP_ENCODER_TO_ANGLE_Y_INTERCEPT) / Constants.RAMP_ENCODER_TO_ANGLE_SLOPE;
    }

    public void setAngle(Rotation2d angleFromGround) {
        setRampPos(angleToEncoder(angleFromGround.getDegrees()));
    }

    private void setFF(double feedForward) {
        rampIO.setFF(feedForward);
    }
    public double getFF(){
        return inputs.rampFF;
    }

    public void updateFF() {
        if (Math.abs(getRampPos() - getDesiredPosition()) <= Constants.RAMP_ELIM_FF_THRESHOLD) {
            if (getFF() != NeoPidMotor.DEFAULT_FF) {
                setFF(NeoPidMotor.DEFAULT_FF);
            }
        } else if (getFF() != Constants.RAMP_PID_FAR_FF) {
            setFF(Constants.RAMP_PID_FAR_FF);
        }
    }

    public double calcPose(Pose2d pose2d, Alignable alignable) {
        return rampAngleMap.get(pose2d.getTranslation().getDistance(new Translation2d(alignable.getX(), alignable.getY())));
    }

    public void setDefaultFF() {
        rampIO.setP(NeoPidMotor.DEFAULT_P);
        rampIO.setFF(NeoPidMotor.DEFAULT_FF);
    }

    public void setFarFF() {
        rampIO.setP(Constants.RAMP_PID_P);
        rampIO.setFF(Constants.RAMP_PID_FAR_FF);
    }
}