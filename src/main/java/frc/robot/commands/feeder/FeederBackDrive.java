package frc.robot.commands.feeder;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.utils.command.TimedSubsystemCommand;

public class FeederBackDrive extends TimedSubsystemCommand<Feeder> {
    public FeederBackDrive(Feeder feeder) {
        super(feeder,Constants.FEEDER_BACK_DRIVE_TIMEOUT);
    }
    @Override
    public void end(boolean interrupted) {
        getSystem().stopFeederMotor();
    }
    @Override
    public void execute() {
        getSystem().setFeederMotorSpeed(Constants.FEEDER_BACK_DRIVE_SPEED);
    }
    @Override
    public boolean isFinished() {
        return getSystem().pieceSeen() || super.isFinished();
    }
}
