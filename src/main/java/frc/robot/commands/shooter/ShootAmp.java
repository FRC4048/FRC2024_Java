package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.BlinkinPattern;
import edu.wpi.first.wpilibj.Timer;

public class ShootAmp extends Command {
    private final Shooter shooter;
    private Timer timer = new Timer();
    private double startTime;
    private boolean leftStarted;
    private boolean rightStarted;
    private final LightStrip lightStrip;

    public ShootAmp(Shooter shooter, LightStrip lightStrip) {
        this.shooter = shooter;
        this.lightStrip = lightStrip;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        leftStarted = false;
        rightStarted = false;
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void execute() {
        if (!leftStarted) {
            shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_AMP_SPEED);
            leftStarted = true;
        }
        if (timer.getFPGATimestamp() - startTime > Constants.SHOOTER_MOTOR_STARTUP_OFFSET && !rightStarted) {
            shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_AMP_SPEED);
            rightStarted = true;
        }
        if (shooter.upToSpeed(Constants.SHOOTER_MOTOR_AMP_SPEED, Constants.SHOOTER_MOTOR_AMP_SPEED)){
            lightStrip.setPattern(BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
        }
    }

    /**
     * @param interrupted if command was interrupted
     */
    @Override
    public void end(boolean interrupted) {
    }
}