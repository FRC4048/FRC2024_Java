package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.logging.Logger;

public class ShootAmp extends Command {
    private final Shooter shooter;
    private Timer timer = new Timer();
    private double startTime;
    private boolean leftStarted;
    private boolean rightStarted;

    public ShootAmp(Shooter shooter) {
        this.shooter = shooter;
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
        if (timer.getFPGATimestamp() - startTime > 0.1) {
            shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_AMP_SPEED);
            rightStarted = true;
        }
    }

    /**
     * @param interrupted if command was interrupted
     */
    @Override
    public void end(boolean interrupted) {
    }
}