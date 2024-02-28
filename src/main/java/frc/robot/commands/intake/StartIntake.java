package frc.robot.commands.intake;

import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.command.TimedSubsystemCommand;

public class StartIntake extends TimedSubsystemCommand<IntakeSubsystem> {

    public StartIntake(IntakeSubsystem intakeSubsystem, int motorRunTime) {
        super(intakeSubsystem,motorRunTime);
    }
  
    @Override
    public void execute() {
        getSystem().setMotorSpeed(Constants.INTAKE_MOTOR_1_SPEED, Constants.INTAKE_MOTOR_2_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        getSystem().stopMotors();
    }
}