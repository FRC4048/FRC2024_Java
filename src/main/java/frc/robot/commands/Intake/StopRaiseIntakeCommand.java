package frc.robot.commands.Intake;

import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public class StopRaiseIntakeCommand extends Command {
    private IntakeSubsystem intakeSubsystem;

    public StopRaiseIntakeCommand(IntakeSubsystem intakeSubsystem) {
        addRequirements(intakeSubsystem);
        this.intakeSubsystem = intakeSubsystem;
    }
  
    @Override
    public void execute() {
        intakeSubsystem.stopRaiseIntake();
    }
}
