package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.utils.TimeoutCounter;

public class StartIntake extends Command {
    private final IntakeSubsystem intakeSubsystem;
    private final Timer timer = new Timer();
    private final int motorRunTime; // temporary until  done testing
    private final TimeoutCounter timeoutCounter = new TimeoutCounter("Start Intake");

    public StartIntake(IntakeSubsystem intakeSubsystem, int motorRunTime ) {
        this.intakeSubsystem = intakeSubsystem;
        this.motorRunTime = motorRunTime;
        addRequirements(intakeSubsystem);
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
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        if (timer.hasElapsed(motorRunTime)) {
            timeoutCounter.increaseTimeoutCount();
            return true;
        }
        return false;
    }
}