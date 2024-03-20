package frc.robot.commands.shooter;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Shooter;
import frc.robot.utils.Alignable;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.ShooterSpeed;

import java.util.function.Supplier;

public class AdvancedSpinningShot extends Command {
    private final Shooter shooter;
    private final Supplier<Pose2d> pose2dSupplier;
    private final Supplier<Alignable> alignableSupplier;
    private final LightStrip lightStrip;
    private Alignable alignable;
    private ShooterSpeed shooterSpeed;
    private static final ShooterSpeed leftShooterSpeed = new ShooterSpeed(Constants.SHOOTER_MOTOR_LOW_SPEED, Constants.SHOOTER_MOTOR_HIGH_SPEED);
    private static final ShooterSpeed rightShooterSpeed = new ShooterSpeed(Constants.SHOOTER_MOTOR_HIGH_SPEED, Constants.SHOOTER_MOTOR_LOW_SPEED);;

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
    }

    @Override
    public void execute() {
        shooter.setShooterMotorLeftRPM(shooterSpeed.getLeftMotorSpeed());
        shooter.setShooterMotorRightRPM(shooterSpeed.getRightMotorSpeed());
        if (shooter.upToSpeed(shooterSpeed.getLeftMotorSpeed(),shooterSpeed.getRightMotorSpeed())){
            lightStrip.setPattern(BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
        }
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stopShooter();
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
