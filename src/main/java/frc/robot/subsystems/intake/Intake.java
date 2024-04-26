package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final IntakeIO intakeIO;
    private final IntakeInputs inputs = new IntakeInputs();

    public Intake(IntakeIO io) {
        this.intakeIO = io;
    }

    public void setMotorSpeed(double motor1Speed, double motor2Speed) {
        intakeIO.setMotorSpeeds(motor1Speed, motor2Speed);
    }

    public void stopMotors() {
        intakeIO.stopMotors();
    }

    @Override
    public void periodic() {
        intakeIO.updateInputs(inputs);
        org.littletonrobotics.junction.Logger.processInputs("intakeInputs", inputs);
    }

    public double getMotor1StatorCurrent() {
        return inputs.intakeMotor1Current;
    }
}
