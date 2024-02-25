package frc.robot.commands.shooter;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.Alignable;
import frc.robot.utils.ShooterSpeed;

import java.util.function.Supplier;

public class AdvancedSpinningShot extends Command {
    private final Shooter shooter;
    private final Supplier<Pose2d> pose2dSupplier;
    private final Timer timer;
    private final Supplier<Alignable> alignable;
    private ShooterSpeed shooterSpeed;

    public AdvancedSpinningShot(Shooter shooter , Supplier<Pose2d> curentPoseSupplier, Supplier<Alignable> alignableSupplier) {
        this.shooter = shooter;
        this.pose2dSupplier = curentPoseSupplier;
        this.alignable = alignableSupplier;
        this.timer = new Timer();
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        timer.reset();
        this.shooterSpeed = calcuateShooterSpeed();
        timer.start();
    }

    @Override
    public void execute() {
        shooter.setShooterMotorLeftSpeed(shooterSpeed.getLeftMotorSpeed());
        shooter.setShooterMotorRightSpeed(shooterSpeed.getRightMotorSpeed());
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.hasElapsed(Constants.ADVANCED_SPINNING_SHOT_TIMEOUT) || alignable.get() == null;
    }

    private ShooterSpeed calcuateShooterSpeed() {
        Pose2d currentPose = pose2dSupplier.get();
        if (currentPose.getY() > Constants.SPEAKER_TOP_EDGE_Y_POS) return new ShooterSpeed(Constants.SHOOTER_MOTOR_LOW_SPEED, Constants.SHOOTER_MOTOR_HIGH_SPEED);
        else return new ShooterSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED, Constants.SHOOTER_MOTOR_LOW_SPEED);
    }

}
