package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

import java.util.function.DoubleSupplier;

public class SpinIntake extends Command {
    private final IntakeSubsystem intakeSubsystem;
    private final DoubleSupplier speed;

    public SpinIntake(IntakeSubsystem intakeSubsystem, DoubleSupplier speed) {
        this.intakeSubsystem = intakeSubsystem;
        this.speed = speed;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.setMotorSpeed(speed.getAsDouble(),speed.getAsDouble());
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
