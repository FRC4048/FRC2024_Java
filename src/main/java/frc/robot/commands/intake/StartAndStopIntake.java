package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;

public class StartAndStopIntake extends Command{
    private IntakeSubsystem intake;
    private boolean intakeMoving;
    private double time;

    public StartAndStopIntake(IntakeSubsystem intake) {
        this.intake = intake;
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopMotors();
    }

    @Override
    public void execute() {
        if (intakeMoving == true) {
            intake.stopMotors();
        } else {
            intake.setMotorSpeed(Constants.INTAKE_MOTOR_1_SPEED, Constants.INTAKE_MOTOR_2_SPEED);
        }
    }

    @Override
    public void initialize() {
        intakeMoving = intake.getMotor1Speed() > 0.1;
        time = Timer.getFPGATimestamp();
    } 

    @Override
    public boolean isFinished() {
        return (intakeMoving == false) || (Timer.getFPGATimestamp() - time > 5);
    }
}
