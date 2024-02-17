package frc.robot.commands.cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;

public class StartIntake extends Command {
    private IntakeSubsystem intakeSubsystem;
    private Timer timer = new Timer();
    private final int MOTOR_RUN_TIME = 5; // temporary until  done testing

    public StartIntake(IntakeSubsystem intakeSubsystem) {
        addRequirements(intakeSubsystem);
        this.intakeSubsystem = intakeSubsystem;
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }
  
    @Override
    public void execute() {
        intakeSubsystem.setMotorSpeed(Constants.INTAKE_MOTOR_1_SPEED, Constants.INTAKE_MOTOR_2_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stopMotors();
    }

    @Override
    public boolean isFinished() {
        if (timer.hasElapsed(MOTOR_RUN_TIME)) {
            return true;
        }

        else {
            return false;
        }
    }
}