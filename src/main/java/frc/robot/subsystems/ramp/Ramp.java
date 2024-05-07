package frc.robot.subsystems.ramp;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LoggableSystem;
import frc.robot.utils.advanced.Alignable;
import frc.robot.utils.motor.NeoPidMotor;


public class Ramp extends SubsystemBase {
    private final LoggableSystem<RampIO, RampInputs> system;
    private final InterpolatingDoubleTreeMap rampAngleMap = new InterpolatingDoubleTreeMap();

    public Ramp(RampIO io) {
        system = new LoggableSystem<>(io, new RampInputs());
        rampAngleMap.put(1.49352, 3.0);
        rampAngleMap.put(1.806956, 5.0);
        rampAngleMap.put(2.3876, 7.56);
        rampAngleMap.put(2.6162, 7.9);
    }

    public void periodic() {
        system.updateInputs();
    }

    public void setRampPos(double targetPosition) {
        system.getIO().setRampPos(targetPosition);
    }
    
    /**
     * This Java Docs is bad
     * Sets the motor speed
     * Check if forward had a positive or negative speed value
     * @param speed the speed (0-1)
     */
    public void setSpeed(double speed) {
        system.getIO().setSpeed(speed);
    }

    public void stopMotor() {
        system.getIO().stopMotor();
    }

    public double getRampPos() {
        return system.getInputs().encoderPosition;
    }

    /**
     * @return If the Forward Limit Switch is pressed
     */
    public boolean getForwardSwitchState() {
        return system.getInputs().fwdTripped;
    }

    /**
     * @return If the Reversed Limit Switch is pressed
     */
    public boolean getReversedSwitchState() {
        return system.getInputs().revTripped;
    }

    public double getDesiredPosition() {
        return system.getInputs().rampTargetPos;
    }

    public void resetEncoder() {
        system.getIO().resetEncoder();
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
        system.getIO().setFF(feedForward);
    }
    public double getFF(){
        return system.getInputs().rampFF;
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
        system.getIO().setP(NeoPidMotor.DEFAULT_P);
        system.getIO().setFF(NeoPidMotor.DEFAULT_FF);
    }

    public void setFarFF() {
        system.getIO().setP(Constants.RAMP_PID_P);
        system.getIO().setFF(Constants.RAMP_PID_FAR_FF);
    }
}