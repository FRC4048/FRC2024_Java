package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Feeder;
import frc.robot.Constants;

public class StartFeeder extends Command {

    private Feeder feeder;

    public StartFeeder(Feeder feeder) {
        this.feeder = feeder;
        addRequirements(feeder);
    }

    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }

    @Override
    public boolean isFinished() {
        if (feeder.getFeederSensor() == true) {
            return true;
        }

        else {
            return false;
        }
    }
}