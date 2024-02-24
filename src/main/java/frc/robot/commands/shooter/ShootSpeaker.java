package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;

public class ShootSpeaker extends Command {

    private final Shooter shooter;
    private final Timer timer = new Timer();
    private boolean activated = false;
    private SwerveDrivetrain drivetrain;
    public ShootSpeaker(Shooter shooter, SwerveDrivetrain drivetrain) {
        this.shooter = shooter;
        this.drivetrain = drivetrain;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        activated = true;
        shooter.setShooterSpeeds(drivetrain);
        timer.start();

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
