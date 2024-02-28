package frc.robot.commands.feeder;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.utils.command.TimedSubsystemCommand;


public class FeederGamepieceUntilLeave extends TimedSubsystemCommand<Feeder> {
    public FeederGamepieceUntilLeave(Feeder feeder) {
        super(feeder, Constants.FEEDER_SHOOT_TIMEOUT);
    }

    @Override
    public void execute() {
        getSystem().setFeederMotorSpeed(Constants.FEEDER_MOTOR_EXIT_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        getSystem().stopFeederMotor();
    }

    @Override
    public boolean isFinished() {
        return getSystem().pieceSeen() || super.isFinished();
    }

}
