package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;

public class StartFeeder extends Command {

    private final Feeder feeder;
    private double startTime;

    public StartFeeder(Feeder feeder) {
        this.feeder = feeder;
        addRequirements(feeder);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_ENTER_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }

    @Override
    public boolean isFinished() {
        return feeder.pieceSeen(true) || Timer.getFPGATimestamp() - startTime > 5.0;
    }
}