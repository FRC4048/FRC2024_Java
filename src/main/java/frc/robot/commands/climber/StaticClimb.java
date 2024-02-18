package frc.robot.commands.climber;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Climber;

public class StaticClimb extends Command {
    private final Climber climber;
    private double startTime;

    public StaticClimb(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }
    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();

    }
    @Override
    public void execute() {
        if (climber.getGyroPitch() > Constants.CLIMBER_BALANCE_THRESH) {
            climber.balanceLeft(Constants.CLIMBER_BALANCE_LOW_SPEED);
        } else if (-climber.getGyroPitch() > Constants.CLIMBER_BALANCE_THRESH) {
            climber.balanceRight(Constants.CLIMBER_BALANCE_LOW_SPEED);
        } else {
            climber.raise(false); // reverse raises down the arms
        }
        
    }
    @Override
    public boolean isFinished() {
        return Timer.getFPGATimestamp() - startTime > Constants.CLIMBER_TIMEOUT_S;
    }
    @Override
    public void end(boolean interrupted) {
        climber.stop();
    }
}