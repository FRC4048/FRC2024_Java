package frc.robot.commands.feeder;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.utils.command.TimedSubsystemCommand;


public class FeederGamepieceUntilLeave extends TimedSubsystemCommand<Feeder> {
    public FeederGamepieceUntilLeave(Feeder feeder) {
        super(feeder,5);
    }
    @Override
    public void execute() {
        getSystem().setFeederMotorSpeed(Constants.FEEDER_MOTOR_EXIT_SPEED);
    }

    @Override
    public boolean isFinished() {
        return getSystem().pieceSeen() || super.isFinished();
    }
    @Override
    public void end(boolean interrupted) {
        getSystem().stopFeederMotor();
    }
    
}
