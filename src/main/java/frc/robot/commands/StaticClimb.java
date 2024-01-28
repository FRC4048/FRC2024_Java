package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;
import frc.robot.utils.ClimberState;
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
        climber.setClimberState(ClimberState.NORMALDOWN);
    }
    @Override
    public void execute() {
        climber.raise(false);
    }
    @Override
    public boolean isFinished() {
        if (climber.getNavxGyroValue() > Constants.CLIMBER_BALANCE_THRESH) {
            return true;
        } else {
            return false;
        }
    }
}
