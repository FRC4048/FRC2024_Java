package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.intake.Intake;
import frc.robot.utils.loggingv2.LoggableCommand;

public class StartIntake extends LoggableCommand {
    private final Intake intake;
    private final Timer timer = new Timer();
    private final double motorRunTime; // temporary until  done testing

    public StartIntake(Intake intake, double motorRunTime ) {
        addRequirements(intake);
        this.intake = intake;
        this.motorRunTime = motorRunTime;
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }
  
    @Override
    public void execute() {
        intake.setMotorSpeed(Constants.INTAKE_MOTOR_1_SPEED, Constants.INTAKE_MOTOR_2_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopMotors();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(motorRunTime);
    }
}