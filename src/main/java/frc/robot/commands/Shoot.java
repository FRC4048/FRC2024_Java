package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
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
        //Rest timers
        timer.reset();
        activated = false;
        shooter.setShooterMotor1RPM(Constants.SHOOTER_MOTOR_1_RPM);
        shooter.setShooterMotor2RPM(Constants.SHOOTER_MOTOR_2_RPM);
    }

    @Override
    public void execute() {
        if (((shooter.getShooterSensor1Activated() == true) || (shooter.getShooterSensor2Activated() == true)) && activated == false) {
            timer.start();
            activated = true;
        }
    }

    @Override 
    public boolean isFinished() {
        if ((timer.hasElapsed(SHOOTER_TIME_AFTER_TRIGGER)) && (activated == true)) {
                return true;
            }

            else {
                return false;
            }
    }

    @Override
    public void end(boolean interrupted) {
        //Stop and reset everything once command has ended
        shooter.stopShooter();
        timer.stop();
        activated = false;
    }
}
