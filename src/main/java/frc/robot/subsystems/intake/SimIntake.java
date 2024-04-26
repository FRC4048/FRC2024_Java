package frc.robot.subsystems.intake;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;

public class SimIntake implements IntakeIO {
    private final FlywheelSim flywheelSim1;
    private final FlywheelSim flywheelSim2;
    private double appliedSpeed1;
    private double appliedSpeed2;

    public SimIntake() {
        DCMotor dcMotor1 = new DCMotor(12, 0.7, 130, 3.8, 2201, 1);
        DCMotor dcMotor2 = new DCMotor(12, 0.7, 130, 3.8, 2201, 1);
        this.flywheelSim1 = new FlywheelSim(dcMotor1,1, 0.001);;
        this.flywheelSim2 = new FlywheelSim(dcMotor2,1, 0.001);;
    }

    @Override
    public void setMotorSpeeds(double m1Speed, double m2Speed) {
        appliedSpeed1 = m1Speed;
        appliedSpeed2 = m2Speed;
        flywheelSim1.setInputVoltage(m1Speed * 12);
        flywheelSim1.setInputVoltage(m2Speed * 12);
    }

    @Override
    public void stopMotors() {
        flywheelSim1.setInputVoltage(0);
        flywheelSim2.setInputVoltage(0);
    }

    @Override
    public void updateInputs(IntakeInputs inputs) {
        flywheelSim1.update(0.02);
        inputs.intakeMotor1Current = flywheelSim1.getCurrentDrawAmps();
        inputs.intakeMotor1Speed = appliedSpeed1;
        inputs.intakeMotor2Speed = appliedSpeed2;
    }
}
