package frc.robot.commands.cannon;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class ShootTest extends Command {

    private final Shooter shooter;
    private final Timer timer = new Timer();
    private final double runTime;

    /**
     * @param shooter shooter subsystem
     * @param runTime how many seconds shooter should run for
     */
    public ShootTest(Shooter shooter, double runTime) {
        this.shooter = shooter;
        this.runTime = runTime;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        shooter.setShooterMotor1RPM(Constants.SHOOTER_MOTOR_1_RPM);
        shooter.setShooterMotor2RPM(Constants.SHOOTER_MOTOR_2_RPM);
        timer.reset();
        timer.start();
    }

    /**
     * @return true of timer has elapsed for {@link #runTime} seconds.
     */
    @Override
    public boolean isFinished() {
        if (timer.advanceIfElapsed(runTime)) {
            shooter.stopShooter();
            return true;
        }
        return false;
    }
    
}
