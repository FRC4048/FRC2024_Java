package frc.robot.commands.intake;

import frc.robot.subsystems.intake.Intake;
import frc.robot.utils.loggingv2.LoggableCommand;

public class StopIntake extends LoggableCommand {
    private final Intake intake;

    public StopIntake(Intake intake) {
        addRequirements(intake);
        this.intake = intake;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.stopMotors();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}