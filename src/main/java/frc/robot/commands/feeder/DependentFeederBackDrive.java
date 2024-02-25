package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.ColorObject;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Deployer;
import frc.robot.subsystems.Feeder;

public class DependentFeederBackDrive extends Command {
    private final Feeder feeder;
    private final Deployer deployer;
    private double time;
    public DependentFeederBackDrive(Feeder feeder, Deployer deployer) {
        this.feeder = feeder;
        this.deployer = deployer;
        addRequirements(feeder);

    }
    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }
    @Override
    public void execute() {
        if (deployer.isDeployerReverseLimitSwitchClosed()) {
            feeder.setFeederMotorSpeed(Constants.FEEDER_BACK_DRIVE_SPEED);
        } else {
            feeder.stopFeederMotor();
        }
    }
    @Override
    public void initialize() {
        time = Timer.getFPGATimestamp();
    }
    @Override
    public boolean isFinished() {
        if (deployer.isDeployerReverseLimitSwitchClosed()) {
            return feeder.getPiece().equals(ColorObject.Piece) || Timer.getFPGATimestamp() - time > 1;
        } else {
            return true;
        }
    }
}

