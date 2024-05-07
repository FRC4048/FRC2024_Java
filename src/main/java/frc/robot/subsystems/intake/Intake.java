package frc.robot.subsystems.intake;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.LoggableSystem;

public class Intake extends SubsystemBase {
    private final LoggableSystem<IntakeIO, IntakeInputs> system;

    public Intake(IntakeIO io) {
        this.system = new LoggableSystem<>(io, new IntakeInputs());
    }

    public void setMotorSpeed(double motor1Speed, double motor2Speed) {
        system.getIO().setMotorSpeeds(motor1Speed, motor2Speed);
    }

    public void stopMotors() {
        system.getIO().stopMotors();
    }

    @Override
    public void periodic() {
        system.updateInputs();
    }

    public double getMotor1StatorCurrent() {
        return system.getInputs().intakeMotor1Current;
    }
}
