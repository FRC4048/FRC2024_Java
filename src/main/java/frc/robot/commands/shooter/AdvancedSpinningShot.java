package frc.robot.commands.shooter;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.shooter.Shooter;
import frc.robot.utils.Alignable;
import frc.robot.utils.ShooterSpeed;
import frc.robot.utils.loggingv2.LoggableCommand;

import java.util.function.Supplier;

public class AdvancedSpinningShot extends LoggableCommand {
    private final Shooter shooter;
    private final Supplier<Pose2d> pose2dSupplier;
    private final Supplier<Alignable> alignableSupplier;
    private final LightStrip lightStrip;
    private Alignable alignable;
    private ShooterSpeed shooterSpeed;
    private static final ShooterSpeed leftShooterSpeed = new ShooterSpeed(Constants.SHOOTER_MOTOR_LOW_SPEED, Constants.SHOOTER_MOTOR_HIGH_SPEED);
    private static final ShooterSpeed rightShooterSpeed = new ShooterSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED, Constants.SHOOTER_MOTOR_LOW_SPEED);;
    private Timer timer = new Timer();

    public AdvancedSpinningShot(Shooter shooter, LightStrip lightStrip, Supplier<Pose2d> curentPoseSupplier, Supplier<Alignable> alignableSupplier) {
        this.shooter = shooter;
        this.pose2dSupplier = curentPoseSupplier;
        this.alignableSupplier = alignableSupplier;
        this.lightStrip = lightStrip;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooterSpeed = calcuateShooterSpeed();
        this.alignable = alignableSupplier.get();
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        if (shooterSpeed.getLeftMotorSpeed()>shooterSpeed.getRightMotorSpeed()) {
            shooter.setShooterMotorLeftRPM(shooterSpeed.getLeftMotorSpeed());
        } else {
            shooter.setShooterMotorRightRPM(shooterSpeed.getRightMotorSpeed());
        }
        if (timer.hasElapsed(Constants.SHOOTER_MOTOR_STARTUP_OFFSET)) {
            if (shooterSpeed.getLeftMotorSpeed()>shooterSpeed.getRightMotorSpeed()) {
                shooter.setShooterMotorRightRPM(shooterSpeed.getRightMotorSpeed());
            } else {
                shooter.setShooterMotorLeftRPM(shooterSpeed.getLeftMotorSpeed());
            }
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.slowStop();
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return alignable == null || alignableSupplier.get() == null || !alignable.equals(alignableSupplier.get());
    }

    private ShooterSpeed calcuateShooterSpeed() {
        Pose2d currentPose = pose2dSupplier.get();
        if (currentPose.getY() > Constants.SPEAKER_TOP_EDGE_Y_POS) {
            return RobotContainer.isRedAlliance() ? rightShooterSpeed : leftShooterSpeed;
        } else {
            return RobotContainer.isRedAlliance() ? leftShooterSpeed : rightShooterSpeed;
        }
    }
}
