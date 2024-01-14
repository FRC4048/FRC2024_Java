package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class Shoot extends Command {

    private Shooter shooter;
    Timer timer = new Timer();

    @Override
    public void initialize() {
        //Rest timer
        timer.reset();
    }

    @Override
    public void execute() {
        //Spin motors once started
        shooter.spinMotors(Constants.SHOOTER_MOTOR_SPEED);
    }

    @Override 
    public boolean isFinished() {
        //Check if sensor has been activated, waits 0.5 seconds, then stops the motors
        if (shooter.getShooterSensorActivated() == true) {
            timer.start();
            if (timer.advanceIfElapsed(0.5)) {
                shooter.stopMotor();
                timer.reset();
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
    
}
