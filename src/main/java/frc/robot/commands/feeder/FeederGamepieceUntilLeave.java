package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Feeder;
import frc.robot.utils.TimeoutCounter;


public class FeederGamepieceUntilLeave extends Command{
    private Feeder feeder;
    private double time;
    private final TimeoutCounter timeoutCounter = new TimeoutCounter("Feeder gamepiece until leave");
    public FeederGamepieceUntilLeave(Feeder feeder) {
        this.feeder = feeder;
        addRequirements(feeder);
        
    }
    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }
    @Override
    public void execute() {
        feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_EXIT_SPEED);
    }
    @Override
    public void initialize() {
        time = Timer.getFPGATimestamp();
    }
    @Override
    public boolean isFinished() {
        if (Timer.getFPGATimestamp() - time > 0.5) {
            return feeder.pieceNotSeen();
        }
        else if (Timer.getFPGATimestamp() - time > Constants.FEEDER_GAMEPIECE_UNTIL_LEAVE_TIMEOUT) {
            timeoutCounter.increaseTimeoutCount();
            return true;
        }
        return false;
    }
    
}
