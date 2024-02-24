package frc.robot.utils.diag;

import com.revrobotics.SparkLimitSwitch;

public class DiagSparkMaxLimit extends DiagBoolean {

    private SparkLimitSwitch limitSwitch;

    public DiagSparkMaxLimit(SparkLimitSwitch limitSwitch, String title, String name) {
        super(title, name);
        this.limitSwitch = limitSwitch;
    }

    @Override
    protected boolean getValue() {
        return limitSwitch.isPressed();
    }    
}
