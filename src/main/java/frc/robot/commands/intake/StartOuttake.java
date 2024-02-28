package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.IntakeSubsystem;

public class StartOuttake extends Command {
    private final IntakeSubsystem intake;
    private final Timer timer = new Timer();
    private final double outtakeTime;

    public StartOuttake(IntakeSubsystem intake, double outtakeTime) {
        this.intake = intake;
        this.outtakeTime = outtakeTime;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        intake.setMotorSpeed(Constants.OUTTAKE_SPEED, Constants.OUTTAKE_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopMotors();
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(outtakeTime);
    }
}
