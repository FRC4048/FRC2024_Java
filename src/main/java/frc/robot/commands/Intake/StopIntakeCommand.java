package frc.robot.commands.Intake;

import frc.robot.subsystems.IntakeSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public class StopIntakeCommand extends Command {
    private IntakeSubsystem intakeSubsystem;

    public StopIntakeCommand(IntakeSubsystem intakeSubsystem, int timeOut) {
        addRequirements(intakeSubsystem);
        this.intakeSubsystem = intakeSubsystem;
    }
  
    @Override
    public void execute() {
        intakeSubsystem.stopMotor();
    }
}