package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.constants.Constants;

public class RealIntake implements IntakeIO {
    private final WPI_TalonSRX intakeMotor1;
    private final WPI_TalonSRX intakeMotor2;

    public RealIntake() {
        this.intakeMotor1 = new WPI_TalonSRX(Constants.INTAKE_MOTOR_1_ID);
        this.intakeMotor2 = new WPI_TalonSRX(Constants.INTAKE_MOTOR_2_ID);
        configureMotor();
    }
    public void configureMotor(){
        intakeMotor1.setInverted(false);
        intakeMotor2.setInverted(false);

        //Configure current limits *VALUES HAVE TO BE TWEAKED*
        intakeMotor1.configPeakCurrentLimit(Constants.INTAKE_MOTOR_PEAK_CURRENT_LIMIT);
        intakeMotor1.configPeakCurrentDuration(Constants.INTAKE_MOTOR_PEAK_CURRENT_DURATION);
        intakeMotor1.configContinuousCurrentLimit(Constants.INTAKE_MOTOR_CONTINUOUS_CURRENT_LIMIT);
        intakeMotor1.enableCurrentLimit(Constants.INTAKE_CURRENT_LIMIT_ENABLED);

        intakeMotor2.configPeakCurrentLimit(Constants.INTAKE_MOTOR_PEAK_CURRENT_LIMIT);
        intakeMotor2.configPeakCurrentDuration(Constants.INTAKE_MOTOR_PEAK_CURRENT_DURATION);
        intakeMotor2.configContinuousCurrentLimit(Constants.INTAKE_MOTOR_CONTINUOUS_CURRENT_LIMIT);
        intakeMotor2.enableCurrentLimit(Constants.INTAKE_CURRENT_LIMIT_ENABLED);

        intakeMotor1.setStatusFramePeriod(1,20);
        intakeMotor1.setStatusFramePeriod(2,100);
        intakeMotor1.setStatusFramePeriod(3,100);
    }
    @Override
    public void setMotorSpeeds(double m1Speed, double m2Speed) {
        intakeMotor1.set(m1Speed);
        intakeMotor2.set(m2Speed);
    }

    @Override
    public void stopMotors() {
        intakeMotor1.stopMotor();
        intakeMotor2.stopMotor();
    }

    @Override
    public void updateInputs(IntakeInputs inputs) {
        inputs.intakeMotor1Speed = intakeMotor1.get();
        inputs.intakeMotor2Speed = intakeMotor2.get();
        inputs.intakeMotor1Current = intakeMotor1.getStatorCurrent();
    }
}
