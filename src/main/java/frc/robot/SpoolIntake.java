package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.intake.Intake;

public class SpoolIntake extends Command {
    private final Intake intake;
    private final Timer timer = new Timer();
    private final double motorRunTime; // temporary until  done testing

    public SpoolIntake(Intake intake, double motorRunTime) {
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
        if (interrupted){
            intake.stopMotors();
        }
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(motorRunTime);
    }
}
