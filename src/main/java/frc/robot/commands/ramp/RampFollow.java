package frc.robot.commands.ramp;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.LightStrip;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.Alignable;
import frc.robot.utils.AutoAlignment;
import frc.robot.utils.BlinkinPattern;
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
    }

    @Override
    public void execute() {
        Alignable alignableNow = drivetrain.getAlignable();
        if (alignableNow != null) {
            double diveTrainXVel = drivetrain.getFieldChassisSpeeds().vxMetersPerSecond * (RobotContainer.isRedAlliance() ? 1 : -1);
            double diveTrainYVel = drivetrain.getFieldChassisSpeeds().vyMetersPerSecond * (RobotContainer.isRedAlliance() ? 1 : -1);
            Pose2d pose = drivetrain.getPose();
            Translation3d rampPose = new Translation3d(pose.getX(), pose.getY(), Constants.ROBOT_FROM_GROUND);
            VelocityVector shooting = AutoAlignment.getYaw(alignable, rampPose, diveTrainXVel);
            //generate future position
            Translation3d deltaPose = new Translation3d(diveTrainXVel * 0.25,diveTrainYVel * 0.25,0);
            Translation3d futurePose = rampPose.plus(deltaPose);
            VelocityVector shootingFuture = AutoAlignment.getYaw(alignable, futurePose, diveTrainXVel);
            if (shooting == null || shootingFuture == null) {
                DriverStation.reportError("Invalid Odometry Can not shoot", true);
                lightStrip.setPattern(BlinkinPattern.BLACK);
                return;
            };
            shooting = new VelocityVector(shooting.getVelocity(), new Rotation2d(Math.PI/2).minus(shooting.getAngle()));
            shootingFuture = new VelocityVector(shootingFuture.getVelocity(), new Rotation2d(Math.PI/2).minus(shootingFuture.getAngle()));
            boolean canReach = shootingFuture.getAngle().getDegrees() != 90 && shootingFuture.getAngle().getDegrees() < Constants.RAMP_MAX_ANGLE;
            if (Constants.RAMP_DEBUG) {
                SmartDashboard.putNumber("RAMP_TARGET_ANGLE", shooting.getAngle().getDegrees());
                SmartDashboard.putBoolean("CAN_AUTO_SHOOT", canReach);
                SmartDashboard.putNumber("DRIVETRAIN X VELOCITY", drivetrain.getFieldChassisSpeeds().vxMetersPerSecond);
                SmartDashboard.putNumber("DELTA X", rampPose.getX() - Alignable.SPEAKER.getX());
            }
            if (!canReach){
                lightStrip.setPattern(BlinkinPattern.BLACK);
                ramp.setAngle(Rotation2d.fromDegrees(Constants.RAMP_MAX_ANGLE));
                return;
            }
            double clamp = MathUtil.clamp(shootingFuture.getAngle().getDegrees(), Constants.RAMP_MIN_ANGLE, Constants.RAMP_MAX_ANGLE);
            ramp.setAngle(Rotation2d.fromDegrees(clamp));
            double threshold = ramp.getRampPos() - Ramp.angleToEncoder(shooting.getAngle().getDegrees());
            boolean isInThresh = Math.abs(threshold) < Constants.RAMP_AT_POS_THRESHOLD;
            SmartDashboard.putNumber("DEST DIFF", threshold);
            if (isInThresh){
                lightStrip.setPattern(BlinkinPattern.VIOLET);
            } else {
                lightStrip.setPattern(BlinkinPattern.BLACK);
            }
        }

    }

    @Override
    public boolean isFinished() {
        return alignable == null || drivetrain.getAlignable() == null || !alignable.equals(drivetrain.getAlignable());
    }
}
