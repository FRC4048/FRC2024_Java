package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class Shoot extends Command {

    private Shooter shooter;
    private Timer timer = new Timer();
    private boolean activated = false;
    private final double SHOOTER_TIME_AFTER_TRIGGER = 0.5;

    public Shoot(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        //Rest timer
        timer.reset();
        activated = false;
    }

    @Override
    public void execute() {
        //Spin motors once started
        shooter.spinMotors(Constants.SHOOTER_MOTOR_SPEED);
        }

    @Override 
    public boolean isFinished() {
        //Check if sensor has been activated
        if ((shooter.getShooterSensorActivated() == true) || (activated == true)) {
            timer.start();
            activated = true;
            if (timer.hasElapsed(SHOOTER_TIME_AFTER_TRIGGER) == true) {
                return true;
            }

            else {
                return false;
            }
        }

        else {
            return false;
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopMotor();
        timer.stop();
    }
}
