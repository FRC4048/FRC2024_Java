package frc.robot.commands.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.BlinkinPattern;
import edu.wpi.first.wpilibj.Timer;

public class ShootSpeaker extends Command {

    private final Shooter shooter;
    private SwerveDrivetrain drivetrain;
    private final LightStrip lightStrip;
    private Timer timer = new Timer();
    private double startTime;

    public ShootSpeaker(Shooter shooter, SwerveDrivetrain drivetrain, LightStrip lightStrip) {
        this.shooter = shooter;
        this.drivetrain = drivetrain;
        this.lightStrip = lightStrip;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
    }

    @Override 
    public boolean isFinished() {
        return true;
    }

    @Override
    public void execute() {

       double gyro = ((((drivetrain.getGyroAngle().getDegrees()) % 360) + 360) % 360); //Gets the gyro value 0-360
        if (((RobotContainer.isRedAlliance() == true) && (gyro > 180)) ||
            ((RobotContainer.isRedAlliance() == false) && (gyro < 180))) {
            shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_HIGH_SPEED);
            if (timer.getFPGATimestamp() - startTime > Constants.SHOOTER_MOTOR_STARTUP_OFFSET) {
                shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_LOW_SPEED);
            }
            lightStrip.scheduleOnTrue(()-> shooter.upToSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED,Constants.SHOOTER_MOTOR_LOW_SPEED), BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
        }
        else {
            shooter.setShooterMotorRightRPM(Constants.SHOOTER_MOTOR_HIGH_SPEED);
            if (timer.getFPGATimestamp() - startTime > Constants.SHOOTER_MOTOR_STARTUP_OFFSET) {
                shooter.setShooterMotorLeftRPM(Constants.SHOOTER_MOTOR_LOW_SPEED);
            }
            lightStrip.scheduleOnTrue(()-> shooter.upToSpeed(Constants.SHOOTER_MOTOR_LOW_SPEED,Constants.SHOOTER_MOTOR_HIGH_SPEED), BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
        }
    }

    /**
     * @param interrupted if command was interrupted
     */
    @Override
    public void end(boolean interrupted) {
    }
}