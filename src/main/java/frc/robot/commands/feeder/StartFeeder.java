package frc.robot.commands.feeder;

import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.utils.command.TimedSubsystemCommand;

public class StartFeeder extends TimedSubsystemCommand<Feeder> {

    public StartFeeder(Feeder feeder) {
        super(feeder,Constants.FEEDER_INTAKE_TIMEOUT);
    }

    @Override
    public void execute() {
        getSystem().setFeederMotorSpeed(Constants.FEEDER_MOTOR_ENTER_SPEED);
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