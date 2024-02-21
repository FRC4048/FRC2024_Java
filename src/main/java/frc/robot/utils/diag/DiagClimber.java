package frc.robot.utils.diag;

import com.revrobotics.CANSparkMax;

public class DiagClimber extends DiagSparkMaxEncoder {
    private double requiredRaise;
    private double requiredLower;
    private boolean loweredTrue;
    private boolean raisedTrue;
    public DiagClimber(String title, String name, double requiredTravel, CANSparkMax canSparkMax, double requiredLower) {
        super(title, name, requiredTravel, canSparkMax);
        this.requiredLower = requiredLower;
        this.requiredRaise = requiredTravel;
        reset();
    }
    public boolean getDiagResultRaised() {
        double currentValue = getCurrentValue();
        if (currentValue <= requiredLower) {
            loweredTrue = true;
            raisedTrue = false;
        } else if (currentValue >= requiredRaise) {
            raisedTrue = true;
            loweredTrue = false;
        } else {
            loweredTrue = false;
            raisedTrue = false;
        }
        return raisedTrue;
    }
    public boolean getDiagResultLowered() {
        double currentValue = getCurrentValue();
        if (currentValue <= requiredLower) {
            loweredTrue = true;
            raisedTrue = false;
        } else if (currentValue >= requiredRaise) {
            raisedTrue = true;
            loweredTrue = false;
        } else {
            loweredTrue = false;
            raisedTrue = false;
        }
        return loweredTrue;
    }
    @Override
    public double getCurrentValue() {
        return super.getCurrentValue();
    }
}
