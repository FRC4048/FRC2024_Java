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
        feeder.setListeningForceStop(true);
    }

    @Override
    public void execute() {
        if (!feeder.forceStopped()) {
            feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_ENTER_SPEED);
        }
    }

    @Override
    public void end(boolean interrupted) {
        feeder.setListeningForceStop(false);
        feeder.stopFeederMotor();
        
    }

    @Override
    public boolean isFinished() {
        return (feeder.pieceSeen(true) ||
                feeder.forceStopped() ||
                Timer.getFPGATimestamp() - startTime > Constants.START_FEEDER_TIMEOUT
        );
    }
}