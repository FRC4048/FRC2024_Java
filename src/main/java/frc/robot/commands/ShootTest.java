package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class ShootTest extends Command {

    private Shooter shooter;
    private Timer timer = new Timer();
    private final double MOTOR_RUN_TIME = 2;

    public ShootTest(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        shooter.spinMotors(Constants.SHOOTER_MOTOR_SPEED);
    }

    @Override 
    public boolean isFinished() {
        if (timer.advanceIfElapsed(MOTOR_RUN_TIME)) {
            shooter.stopMotor();
            return true;
        }

        else {
            return false;
        }
    }
    
}
