package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.IntakeSubsystem;

public class StartAndStopFeeder extends Command{
    private IntakeSubsystem intake;
    private Deployer deployer;
    private Feeder feeder;
    private boolean intakeMoving;
    private double time;

    public StartAndStopFeeder(IntakeSubsystem intake, Feeder feeder, Deployer deployer) {
        this.intake = intake;
        this.feeder = feeder;
        this.deployer = deployer;
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }

    @Override
    public void execute() {
        if (deployer.isDeployerReverseLimitSwitchClosed() && intake.getMotor1Speed() > 0.1) {
            feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_ENTER_SPEED);
        }
    }

    @Override
    public void initialize() {
        time = Timer.getFPGATimestamp();
    } 

    @Override
    public boolean isFinished() {
        return (feeder.pieceSeen()) || (Timer.getFPGATimestamp() - time > 5);
    }
    
}
