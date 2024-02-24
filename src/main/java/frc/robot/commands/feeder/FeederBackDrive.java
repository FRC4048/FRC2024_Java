package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.utils.ColorObject;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;

public class FeederBackDrive extends Command {
    private final Feeder feeder;
    private double time;
    public FeederBackDrive(Feeder feeder) {
        this.feeder = feeder;
        addRequirements(feeder);

    }
    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }
    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(Constants.FEEDER_BACK_DRIVE_SPEED);
    }
    @Override
    public void initialize() {
        time = Timer.getFPGATimestamp();
    }
    @Override
    public boolean isFinished() {
        return feeder.getPiece().equals(ColorObject.Piece) || Timer.getFPGATimestamp() - time > 1;
    }
}
