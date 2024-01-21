package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.IntakeSubsystem;

public class RaiseIntakeCommand extends Command {
    private IntakeSubsystem intakeSubsystem;

    public RaiseIntakeCommand(IntakeSubsystem intakeSubsystem) {
        addRequirements(intakeSubsystem);
    }
  
    @Override
    public void execute() {
        intakeSubsystem.raiseIntake(Constants.INTAKE_RAISE_MOTOR_SPEED);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
