package frc.robot.commands.intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class StopIntake extends Command{
     private final IntakeSubsystem intakeSubsystem;

    public StopIntake(IntakeSubsystem intakeSubsystem) {
        addRequirements(intakeSubsystem);
        this.intakeSubsystem = intakeSubsystem;
        
    }

    @Override
    public void initialize() {
        intakeSubsystem.setMotorSpeed(0, 0);
    }
    
  
    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return true;    
    }
}
