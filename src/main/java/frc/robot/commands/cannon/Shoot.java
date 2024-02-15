package frc.robot.commands.cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class Shoot extends Command {

    private final Shooter shooter;
    private final Timer timer = new Timer();
    private boolean activated = false;
    public Shoot(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        activated = false;
        shooter.setShooterMotor1RPM(Constants.SHOOTER_MOTOR_1_RPM);
        shooter.setShooterMotor2RPM(Constants.SHOOTER_MOTOR_2_RPM);
    }

    @Override
    public void execute() {
        if (((shooter.getShooterSensor1Activated()) || (shooter.getShooterSensor2Activated())) && !activated) {
            timer.start();
            activated = true;
        }
    }

    @Override 
    public boolean isFinished() {
        return (timer.hasElapsed(Constants.SHOOTER_TIME_AFTER_TRIGGER)) && (activated);
    }

    /**
     * @param interrupted if command was interrupted
     *  stop shooter and timer. Set activated to false
     */
    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
        timer.stop();
        activated = false;
    }
}
