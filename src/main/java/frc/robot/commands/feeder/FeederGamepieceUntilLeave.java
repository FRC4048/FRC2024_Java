package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.TimeoutCounter;


public class FeederGamepieceUntilLeave extends Command {
    private final Feeder feeder;
    private final Ramp ramp;
    private double time;
    private int pieceNotFoundCounter;
    private final TimeoutCounter timeoutCounter = new TimeoutCounter("Feeder gamepiece until leave");
    public FeederGamepieceUntilLeave(Feeder feeder, Ramp ramp) {
        this.feeder = feeder;
        this.ramp = ramp;
        addRequirements(feeder);
    }

    @Override
    public void end(boolean interrupted) {
        feeder.stopFeederMotor();
    }

    @Override
    public void execute() {
        if (ramp.getDesiredPosition() == Constants.RAMP_POS_SHOOT_AMP) {
            feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_AMP_SPEED);
        } else {
            feeder.setFeederMotorSpeed(Constants.FEEDER_MOTOR_SPEAKER_SPEED);
        }
    }

    @Override
    public void initialize() {
        feeder.switchFeederBeamState(false);
        time = Timer.getFPGATimestamp();
        this.pieceNotFoundCounter = 0;
    }

    @Override
    public boolean isFinished() {
        if (!feeder.pieceSeen(true)) {
            pieceNotFoundCounter++;
        }
        if (pieceNotFoundCounter > GameConstants.FEEDER_PIECE_NOT_SEEN_COUNTER) {
            return true;
        }
        else if (Timer.getFPGATimestamp() - time > Constants.FEEDER_GAMEPIECE_UNTIL_LEAVE_TIMEOUT) {
            timeoutCounter.increaseTimeoutCount();
            return true;
        }
        else {
            return false;
        }
    }
}
