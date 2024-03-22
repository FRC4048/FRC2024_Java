package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.LightStrip;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.TimeoutCounter;

public class FeederBackDrive extends Command {
    private final Feeder feeder;
    private final LightStrip lightStrip;
    private double time;
    private TimeoutCounter timeoutCounter;
    public FeederBackDrive(Feeder feeder, LightStrip lightStrip) {
        this.feeder = feeder;
        this.lightStrip = lightStrip;
        timeoutCounter = new TimeoutCounter("FeedBackDrive", lightStrip);
        addRequirements(feeder);

    }
    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
        if (feeder.pieceSeen(false)){
            lightStrip.setPattern(BlinkinPattern.GREEN);
        }
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
        if (feeder.pieceSeen(false)) {
            return true;
        }
        else if(Timer.getFPGATimestamp() - time > Constants.FEEDER_BACK_DRIVE_TIMEOUT) {
            timeoutCounter.increaseTimeoutCount();
            return true;
        }
        return false;
    }
}
