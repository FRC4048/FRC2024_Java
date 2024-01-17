package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class Shoot extends Command {

    private Shooter shooter;
    private Timer timer = new Timer();
    public static boolean isDone = false;
    public static boolean shooterSensorActivated = false;
    private final double SHOOTER_TIME_AFTER_TRIGGER = 0.5;

    public Shoot(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        //Rest timer
        timer.reset();
        isDone = false;
        shooterSensorActivated = shooter.getShooterSensorActivated();
    }

    @Override
    public void execute() {
        //Spin motors once started
        shooter.spinMotors(Constants.SHOOTER_MOTOR_SPEED);
        shooterSensorActivated = shooter.getShooterSensorActivated();

        if (timer.advanceIfElapsed(SHOOTER_TIME_AFTER_TRIGGER)) {
            shooter.stopMotor();
            timer.reset();
            isDone = true;
        }
    }

    @Override 
    public boolean isFinished() {
        //Check if sensor has been activated
        if (shooterSensorActivated == true) {
            timer.start();
            return isDone;
        }

        else {
            return isDone;
        }
    }
    
}
