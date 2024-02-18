package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;
import frc.robot.utils.AutoAlignment;

import java.util.function.DoubleSupplier;

public class Drive extends Command {
    private final SwerveDrivetrain drivetrain;

    private final DoubleSupplier fwdSupplier;
    private final DoubleSupplier strSupplier;
    private final DoubleSupplier rtSupplier;
    private boolean shouldFlip;

    public Drive(SwerveDrivetrain drivetrain, DoubleSupplier fwdSupplier, DoubleSupplier strSupplier, DoubleSupplier rtSupplier) {
        this.drivetrain = drivetrain;
        this.fwdSupplier = fwdSupplier;
        this.strSupplier = strSupplier;
        this.rtSupplier = rtSupplier;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        this.shouldFlip = RobotContainer.isRedAlliance();
    }

    @Override
    public void execute() {
        Alignable alignable = drivetrain.getAlignable();
        double fwd = MathUtil.applyDeadband(fwdSupplier.getAsDouble()*Constants.MAX_VELOCITY,0.3)*-1;
        double str = MathUtil.applyDeadband(strSupplier.getAsDouble()*Constants.MAX_VELOCITY, 0.3)*-1;
        ChassisSpeeds driveStates;
        if (alignable == null){
            double rcw = MathUtil.applyDeadband(rtSupplier.getAsDouble() * Constants.MAX_VELOCITY, 0.3);
            drivetrain.setFacingTarget(false);
            driveStates = drivetrain.createChassisSpeeds(fwd * (shouldFlip ? 1 : -1), str * (shouldFlip ? 1 : -1), -rcw, Constants.FIELD_RELATIVE);
        } else {
            double rcw = AutoAlignment.calcTurnSpeed(alignable, drivetrain.getPose()) * Constants.MAX_ANGULAR_SPEED;
            drivetrain.setFacingTarget(AutoAlignment.angleFromTarget(alignable,drivetrain.getPose()) < Constants.AUTO_ALIGN_THRESHOLD);
            driveStates = drivetrain.createChassisSpeeds(fwd * (shouldFlip ? 1 : -1), str * (shouldFlip ? 1 : -1), rcw, Constants.FIELD_RELATIVE);
        }
        drivetrain.drive(driveStates);
    }


    @Override
    public boolean isFinished() {
        return false;
    }
}