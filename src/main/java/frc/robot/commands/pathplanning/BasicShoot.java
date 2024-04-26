package frc.robot.commands.pathplanning;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.BlinkinPattern;

public class BasicShoot extends Command {
    private final Shooter shooter;
    private final Timer timer = new Timer();
    private boolean activated = false;
    private final double time;
    private final LightStrip lightStrip;
    private double startTime;

    public BasicShoot(Shooter shooter, LightStrip lightStrip, double time) {
        this.shooter = shooter;
        this.time = time;
        this.lightStrip = lightStrip;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        timer.start();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_HIGH_SPEED);
        if (Timer.getFPGATimestamp() - startTime > Constants.SHOOTER_MOTOR_STARTUP_OFFSET){
            shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_LOW_SPEED);
            // Activated to let us know we've started the second motor
            activated = true;
        }
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
        timer.stop();
        activated = false;
    }
}
