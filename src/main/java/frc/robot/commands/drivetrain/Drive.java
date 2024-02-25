package frc.robot.commands.drivetrain;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.Alignable;
import frc.robot.utils.AutoAlignment;
import frc.robot.utils.command.SubsystemCommandBase;

import java.util.function.DoubleSupplier;

public class Drive extends SubsystemCommandBase<SwerveDrivetrain> {
    private final DoubleSupplier fwdSupplier;
    private final DoubleSupplier strSupplier;
    private final DoubleSupplier rtSupplier;
    private boolean shouldFlip;

    public Drive(SwerveDrivetrain drivetrain, DoubleSupplier fwdSupplier, DoubleSupplier strSupplier, DoubleSupplier rtSupplier) {
        super(drivetrain);
        this.fwdSupplier = fwdSupplier;
        this.strSupplier = strSupplier;
        this.rtSupplier = rtSupplier;
    }

    @Override
    public void initialize() {
        this.shouldFlip = RobotContainer.isRedAlliance();
    }

    @Override
    public void execute() {
        Alignable alignable = getSystem().getAlignable();
        double fwd = MathUtil.applyDeadband(fwdSupplier.getAsDouble(), 0.1) * Constants.MAX_VELOCITY;
        double str = MathUtil.applyDeadband(strSupplier.getAsDouble(), 0.1) * Constants.MAX_VELOCITY;
        ChassisSpeeds driveStates;
        if (alignable == null) {
            double rcw = MathUtil.applyDeadband(rtSupplier.getAsDouble(), 0.1) * Constants.MAX_VELOCITY;
            getSystem().setFacingTarget(false);
            driveStates = getSystem().createChassisSpeeds(fwd * (shouldFlip ? 1 : -1), str * (shouldFlip ? 1 : -1), -rcw, Constants.FIELD_RELATIVE);
        } else {
            double rcw = AutoAlignment.calcTurnSpeed(alignable, getSystem().getPose(), getSystem().getAlignableTurnPid()) * Constants.MAX_VELOCITY;
            getSystem().setFacingTarget(AutoAlignment.angleFromTarget(alignable, getSystem().getPose()) < Constants.AUTO_ALIGN_THRESHOLD);
            driveStates = getSystem().createChassisSpeeds(fwd * (shouldFlip ? 1 : -1), str * (shouldFlip ? 1 : -1), rcw, Constants.FIELD_RELATIVE);
        }
        getSystem().drive(driveStates);
    }


    @Override
    public boolean isFinished() {
        return false;
    }
}