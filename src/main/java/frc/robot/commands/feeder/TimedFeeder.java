package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;

public class TimedFeeder extends Command {
    private final Feeder feeder;
    private final Timer timer = new Timer();
    private final double motorRunTime; // temporary until  done testing

    public TimedFeeder(Feeder feeder, double motorRunTime) {
        this.feeder = feeder;
        this.motorRunTime = motorRunTime;
        addRequirements(feeder);
    }

    @Override
    public void initialize() {
        feeder.switchFeederBeamState(false);
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_SPEAKER_SPEED);
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(motorRunTime);
    }
}
