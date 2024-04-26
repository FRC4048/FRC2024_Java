package frc.robot.commands.ramp;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.subsystems.lightstrip.LightStrip;
import frc.robot.subsystems.ramp.Ramp;
import frc.robot.utils.Alignable;
import frc.robot.utils.AutoAlignment;
import frc.robot.utils.BlinkinPattern;
import frc.robot.utils.math.AngleUtils;
import frc.robot.utils.math.PoseUtils;
import frc.robot.utils.math.VelocityVector;

public class RampFollow extends Command {
    private final Ramp ramp;
    private final SwerveDrivetrain drivetrain;
    private final LightStrip lightStrip;
    private Alignable alignable;

    public RampFollow(Ramp ramp, SwerveDrivetrain drivetrain, LightStrip lightStrip) {
        this.ramp = ramp;
        this.drivetrain = drivetrain;
        this.lightStrip = lightStrip;
        addRequirements(ramp);
    }

    @Override
    public void initialize() {
        alignable = drivetrain.getAlignable();
    }

    @Override
    public void end(boolean interrupted) {
        lightStrip.setPattern(BlinkinPattern.BLACK);
        ramp.setRampPos(Constants.RAMP_POS_STOW);
    }

    @Override
    public void execute() {
        Alignable alignableNow = drivetrain.getAlignable();
        if (alignableNow != null) {
            double driveTrainXVel = drivetrain.getFieldChassisSpeeds().vxMetersPerSecond;
            double driveTrainYVel = drivetrain.getFieldChassisSpeeds().vyMetersPerSecond;
            Translation3d rampPose = PoseUtils.addDimension(drivetrain.getPose().getTranslation(), Constants.ROBOT_FROM_GROUND);
            double speakerRelativeXVel = driveTrainXVel * -1;
            VelocityVector shooting = AutoAlignment.getYaw(alignable, rampPose, speakerRelativeXVel);
            Translation3d futurePose = PoseUtils.getFieldEstimatedFuturePose(rampPose, driveTrainXVel, driveTrainYVel, 0.0, 0.05);
            VelocityVector shootingFuture = AutoAlignment.getYaw(alignable, futurePose, speakerRelativeXVel);
            if (shooting == null || shootingFuture == null) {
                DriverStation.reportError("Invalid Odometry Can not shoot", true);
                lightStrip.setPattern(BlinkinPattern.BLACK);
                ramp.updateFF();
                return;
            };
            shooting = new VelocityVector(shooting.getVelocity(), AngleUtils.compliment(shooting.getAngle()));
            shootingFuture = new VelocityVector(shootingFuture.getVelocity(), AngleUtils.compliment(shootingFuture.getAngle()));
            boolean canReach = shootingFuture.getAngle().getDegrees() != 90 &&
                    shooting.getAngle().getDegrees() != 90 &&
                    shootingFuture.getAngle().getDegrees() < Constants.RAMP_MAX_ANGLE &&
                    shooting.getAngle().getDegrees() < Constants.RAMP_MAX_ANGLE;
            if (Constants.RAMP_DEBUG) {
                SmartDashboard.putNumber("RAMP_TARGET_ANGLE", shooting.getAngle().getDegrees());
                SmartDashboard.putBoolean("CAN_AUTO_SHOOT", canReach);
                SmartDashboard.putNumber("DRIVETRAIN X VELOCITY", drivetrain.getFieldChassisSpeeds().vxMetersPerSecond);
                SmartDashboard.putNumber("DELTA X", rampPose.getX() - Alignable.SPEAKER.getX());
            }
            if (!canReach){
                lightStrip.setPattern(BlinkinPattern.BLACK);
                ramp.setAngle(Rotation2d.fromDegrees(Constants.RAMP_MAX_ANGLE));
                ramp.updateFF();
                return;
            }
            double clamp = MathUtil.clamp(shootingFuture.getAngle().getDegrees(), Constants.RAMP_MIN_ANGLE, Constants.RAMP_MAX_ANGLE);
            ramp.setAngle(Rotation2d.fromDegrees(clamp));
            double threshold = Ramp.angleToEncoder(shooting.getAngle().getDegrees()) - ramp.getRampPos();
            boolean isInThresh = Math.abs(threshold) < Constants.RAMP_AT_POS_THRESHOLD;
            if (Constants.RAMP_DEBUG){
                SmartDashboard.putNumber("DEST DIFF", threshold);
            }
            org.littletonrobotics.junction.Logger.recordOutput("ramp/diffFromTarget", threshold);
            if (isInThresh){
                lightStrip.setPattern(BlinkinPattern.VIOLET);
            } else {
                lightStrip.setPattern(BlinkinPattern.BLACK);
            }
            ramp.updateFF();
        }

    }

    @Override
    public boolean isFinished() {
        return alignable == null || drivetrain.getAlignable() == null || !alignable.equals(drivetrain.getAlignable());
    }
}
