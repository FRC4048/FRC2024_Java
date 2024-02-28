package frc.robot.commands.intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;

public class StartAndStopIntake extends Command{
    private IntakeSubsystem intake;
    private double time;
    private Deployer deployer;

    public StartAndStopIntake(IntakeSubsystem intake, Deployer deployer) {
        this.intake = intake;
        this.deployer = deployer;
        addRequirements(intake);
    }

    @Override
    public void end(boolean interrupted) {
        intake.stopMotors();
    }

    @Override
    public void execute() {
        intake.setMotorSpeed(Constants.INTAKE_MOTOR_1_SPEED, Constants.INTAKE_MOTOR_2_SPEED);
    }

    @Override
    public void initialize() {
        time = Timer.getFPGATimestamp();
    } 

    @Override
    public boolean isFinished() {
        return deployer.isDeployerReverseLimitSwitchClosed() == false || (Timer.getFPGATimestamp() - time > 5);
    }
}
