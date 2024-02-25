package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;

public class ShootAmp extends Command{
    private final Shooter shooter;
    private final Timer timer = new Timer();
    private boolean activated = false;
    public ShootAmp(Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        activated = true;
        timer.start();

    }

    @Override 
    public boolean isFinished() {
        return (timer.hasElapsed(Constants.SHOOTER_TIME_AFTER_TRIGGER)) && (activated);
    }

    @Override
    public void execute() {
        shooter.setShooterMotorRightSpeed(Constants.SHOOTER_MOTOR_AMP_SPEED);
        shooter.setShooterMotorLeftSpeed(Constants.SHOOTER_MOTOR_AMP_SPEED);
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
