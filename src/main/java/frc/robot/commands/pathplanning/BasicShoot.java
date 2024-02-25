package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;

public class BasicShoot extends Command {
    private final Shooter shooter;
    private final Timer timer = new Timer();
    private boolean activated = false;
    private final double time;
    public BasicShoot(Shooter shooter, double time) {
        this.shooter = shooter;
        this.time = time;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        activated = true;
        shooter.setShooterMotorLeftSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED);
        shooter.setShooterMotorRightSpeed(Constants.SHOOTER_MOTOR_LOW_SPEED);
        timer.start();

    }

    @Override
    public boolean isFinished() {
        return (timer.hasElapsed(time)) && (activated);
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
