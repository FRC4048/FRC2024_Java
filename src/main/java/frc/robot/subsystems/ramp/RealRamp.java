package frc.robot.subsystems.ramp;

import frc.robot.constants.Constants;
import frc.robot.utils.NeoPidMotor;

public class RealRamp implements RampIO {

    private final NeoPidMotor neoPidMotor;
    private double p;
    private double i;
    private double d;
    private double ff;

    public RealRamp() {
        neoPidMotor = new NeoPidMotor(Constants.RAMP_ID);
        configureMotor();
        resetEncoder();
        neoPidMotor.enableDiagnostics("Ramp", true, true);
    }

    private void configureMotor() {
        neoPidMotor.setSmartMotionAllowedClosedLoopError(Constants.RAMP_ERROR_RANGE);
        neoPidMotor.setMaxAccel(Constants.RAMP_MAX_RPM_ACCELERATION);
        setP(Constants.RAMP_PID_P);
        setFF(Constants.RAMP_PID_FAR_FF);
    }

    @Override
    public void setP(double p) {
        neoPidMotor.getPidController().setP(p);
        this.p = p;
    }

    @Override
    public void setI(double i) {
        neoPidMotor.getPidController().setI(i);
        this.i = i;
    }

    @Override
    public void setD(double d) {
        neoPidMotor.getPidController().setD(d);
        this.d = d;
    }

    @Override
    public void setFF(double ff) {
        neoPidMotor.getPidController().setFF(ff);
        this.ff = ff;
    }

    @Override
    public void setRampPos(double targetPos) {
        neoPidMotor.setPidPos(targetPos);
    }

    @Override
    public void setSpeed(double spd) {
        neoPidMotor.getNeoMotor().set(spd);
    }

    @Override
    public void stopMotor() {
        neoPidMotor.getNeoMotor().stopMotor();
    }

    @Override
    public void resetEncoder() {
        neoPidMotor.resetEncoderPosition();
    }

    @Override
    public void updateInputs(RampInputs inputs) {
        inputs.rampP = p;
        inputs.rampI = i;
        inputs.rampD = d;
        inputs.rampFF = ff;
        inputs.fwdTripped = neoPidMotor.forwardLimitSwitchIsPressed();
        inputs.revTripped = neoPidMotor.reversedLimitSwitchIsPressed();
        inputs.rampTargetPos = neoPidMotor.getSetPosition();
        inputs.encoderPosition = neoPidMotor.getCurrentPosition();
    }
}
