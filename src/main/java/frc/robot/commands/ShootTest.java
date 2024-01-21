package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class ShootTest extends Command {

    private Shooter shooter;
    private Timer timer = new Timer();
    private final double MOTOR_RUN_TIME = 20;

    public ShootTest(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        //Reset and start timers
        shooter.setShooterMotor1RPM(Constants.SHOOTER_MOTOR_1_RPM);
        shooter.setShooterMotor2RPM(Constants.SHOOTER_MOTOR_2_RPM);
        timer.reset();
        timer.start();
    }

    @Override 
    public boolean isFinished() {
        //Check is timer has passed 2 seconds
        if (timer.advanceIfElapsed(MOTOR_RUN_TIME)) {
            shooter.stopShooter();
            return true;
        }

        else {
            return false;
        }
    }
    
}
