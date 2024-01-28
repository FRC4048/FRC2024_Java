package frc.robot.commands;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;
import frc.robot.Constants;

public class StaticClimb extends Command {
    private final Climber climber;
    private int counter;
    private double startTime;

    public StaticClimb(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }
    @Override
    public void initialize() {
        System.out.println("Im Climbing");
        startTime=Timer.getFPGATimestamp();

    }
    @Override
    public void execute() {
        if (climber.getGyroPitch() > Constants.CLIMBER_BALANCE_THRESH) {
            climber.balanceleft(Constants.CLIMBER_BALANCE_LOW_SPEED);
        } else if (-climber.getGyroPitch()>Constants.CLIMBER_BALANCE_THRESH) {
            climber.balanceright(Constants.CLIMBER_BALANCE_LOW_SPEED);
        } else {
            climber.raise(false);
        }
        
    }
    @Override
    public boolean isFinished() {
        if (Timer.getFPGATimestamp()-startTime>10) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void end(boolean interrupted) {
        climber.stop();
    }
}
