package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class LowerIntakeCommand extends Command {
    private IntakeSubsystem intakeSubsystem;

    public LowerIntakeCommand(IntakeSubsystem intakeSubsystem) {
        addRequirements(intakeSubsystem);
    }
  
    @Override
    public void execute() {
        intakeSubsystem.raiseIntake(-0.3); // INTAKE_RAISE_MOTOR_SPEED constant
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}