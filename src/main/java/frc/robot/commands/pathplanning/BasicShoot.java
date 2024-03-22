package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.BlinkinPattern;

public class BasicShoot extends Command {
    private final Shooter shooter;
    private final Timer timer = new Timer();
    private boolean activated = false;
    private final double time;
    private final LightStrip lightStrip;

    public BasicShoot(Shooter shooter, LightStrip lightStrip, double time) {
        this.shooter = shooter;
        this.time = time;
        this.lightStrip = lightStrip;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        activated = true;
        shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_HIGH_SPEED);
        shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_LOW_SPEED);
        timer.start();
    }

    @Override
    public void execute() {
        if (shooter.upToSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED, Constants.SHOOTER_MOTOR_LOW_SPEED)){
            lightStrip.setPattern(BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
        }
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
