package frc.robot.commands.shooter;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.Alignable;
import frc.robot.utils.ShooterSpeed;

import java.util.function.Supplier;

public class AdvancedSpinningShot extends Command {
    private final Shooter shooter;
    private final Supplier<Pose2d> pose2dSupplier;
    private final Supplier<Alignable> alignable;
    private ShooterSpeed shooterSpeed;
    private static final ShooterSpeed leftShooterSpeed = new ShooterSpeed(Constants.SHOOTER_MOTOR_LOW_SPEED, Constants.SHOOTER_MOTOR_HIGH_SPEED);
    private static final ShooterSpeed rightShooterSpeed = new ShooterSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED, Constants.SHOOTER_MOTOR_LOW_SPEED);;

    public AdvancedSpinningShot(Shooter shooter , Supplier<Pose2d> curentPoseSupplier, Supplier<Alignable> alignableSupplier) {
        this.shooter = shooter;
        this.pose2dSupplier = curentPoseSupplier;
        this.alignable = alignableSupplier;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
        this.shooterSpeed = calcuateShooterSpeed();
    }

    @Override
    public void execute() {
        shooter.setShooterMotorLeftRPM(shooterSpeed.getLeftMotorSpeed());
        shooter.setShooterMotorRightRPM(shooterSpeed.getRightMotorSpeed());
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
    }

    @Override
    public boolean isFinished() {
        return false;
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
