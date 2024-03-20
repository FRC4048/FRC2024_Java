package frc.robot.commands.climber;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;
import frc.robot.constants.Constants;

public class ClimberTorture extends Command{
    private Climber climber;
    private Timer timer;
    private int direction = 1;
    private double previousTime=0;
    public ClimberTorture(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }
    @Override
    public void initialize() {
        climber.setSpeed(0);
        climber.resetEncoders();
        timer.start();
    }
    @Override
    public void execute() {
        if (timer.get()-timer.get()%1>previousTime) {
            previousTime = timer.get()-timer.get()%1;
            direction = -direction;
            climber.setSpeed(direction*Constants.CLIMBER_TORTURE_SPEED);
        }
    }
    @Override
    public boolean isFinished() {
        return timer.get() >= 10;
    }
    @Override
    public void end(boolean interrupted) {
        climber.setSpeed(0);
    }


}