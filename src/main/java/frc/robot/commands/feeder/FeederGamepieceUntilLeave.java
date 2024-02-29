package frc.robot.commands.feeder;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.constants.GameConstants;
import frc.robot.subsystems.Feeder;


public class FeederGamepieceUntilLeave extends Command {
    private final Feeder feeder;
    private double time;
    private int pieceNotFoundCounter;
    private int pieceNotFoundCounter;
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
