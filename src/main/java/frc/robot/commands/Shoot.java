package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class Shoot extends Command {

    private Shooter shooter;
    private Timer timer = new Timer();
    private boolean isDone = false;
    private final double SHOOTER_TIME_AFTER_TRIGGER = 0.5;

    @Override
    public void initialize() {
        //Rest timer
        timer.reset();
    }

    @Override
    public void execute() {
        //Spin motors once started
        shooter.spinMotors(Constants.SHOOTER_MOTOR_SPEED);

        if (timer.advanceIfElapsed(SHOOTER_TIME_AFTER_TRIGGER)) {
            shooter.stopMotor();
            timer.reset();
            isDone = true;
        }
    }

    @Override 
    public boolean isFinished() {
        //Check if sensor has been activated
        if (shooter.getShooterSensorActivated() == true) {
            timer.start();
            return false;
        }

        else {
            return isDone;
        }
    }
    
}
