package frc.robot.commands.ramp;

import edu.wpi.first.math.geometry.Rotation2d;
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
            double shootingSpeed;
            if (Constants.SHOOT_WHILE_MOVING) {
                shootingSpeed = Constants.SHOOTER_VELOCITY
                        + (drivetrain.getFieldChassisSpeeds().vxMetersPerSecond
                        * (RobotContainer.isRedAlliance() ? 1 : -1));
            } else {
                shootingSpeed = Constants.SHOOT_AMP_MOTOR_SPEED;
            }
            Rotation2d targetAngle = new Rotation2d(Math.PI / 2) //complementarity angle
                    .minus(AutoAlignment.getYaw(alignableNow,
                            drivetrain.getPose().getTranslation(),
                            Constants.HIGHT_OF_RAMP, shootingSpeed)
                    );
            if (Constants.RAMP_DEBUG) {
                SmartDashboard.putNumber("RAMP_TARGET_Angle", targetAngle.getDegrees());
            }
            ramp.setAngle(targetAngle);
        }
    }

    @Override
    public boolean isFinished() {
        return alignable == null || drivetrain.getAlignable() == null || !alignable.equals(drivetrain.getAlignable());
    }
}
