package frc.robot.commands.ramp;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.Alignable;
import frc.robot.utils.AutoAlignment;

public class RampFollow extends Command {
    private final Ramp ramp;
    private final SwerveDrivetrain drivetrain;
    private Alignable alignable;

    public RampFollow(Ramp ramp, SwerveDrivetrain drivetrain) {
        this.ramp = ramp;
        this.drivetrain = drivetrain;
        addRequirements(ramp);
    }

    @Override
    public void initialize() {
        alignable = drivetrain.getAlignable();
    }

    @Override
    public void execute() {
        Alignable alignableNow = drivetrain.getAlignable();
        if (alignableNow != null) {
            double shootingSpeed = Constants.SHOOTER_VELOCITY;
            if (Constants.SHOOT_WHILE_MOVING) {
                shootingSpeed += drivetrain.getFieldChassisSpeeds().vxMetersPerSecond * (RobotContainer.isRedAlliance() ? 1 : -1);
            }
            Pose2d pose = drivetrain.getPose();
            Translation3d rampPose = new Translation3d(pose.getX(), pose.getY(), Constants.ROBOT_FROM_GROUND);
            Rotation2d targetAngle = new Rotation2d(Math.PI / 2).minus(AutoAlignment.getYaw(alignable, rampPose, shootingSpeed));
            if (Constants.RAMP_DEBUG) {
                SmartDashboard.putNumber("RAMP_TARGET_ANGLE", targetAngle.getDegrees());
                SmartDashboard.putBoolean("CAN_AUTO_SHOOT", targetAngle.getDegrees() != 90);
                SmartDashboard.putNumber("DRIVETRAIN X VELOCITY", drivetrain.getFieldChassisSpeeds().vxMetersPerSecond);
            }
            double clamp = MathUtil.clamp(targetAngle.getDegrees(), Constants.RAMP_MIN_ANGLE, Constants.RAMP_MAX_ANGLE);
            ramp.setAngle(Rotation2d.fromDegrees(clamp));
        }
    }

    @Override
    public boolean isFinished() {
        return alignable == null || drivetrain.getAlignable() == null || !alignable.equals(drivetrain.getAlignable());
    }
}
