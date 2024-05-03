package frc.robot.commands.drivetrain;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;
import frc.robot.swervev3.SwerveDrivetrain;
import frc.robot.utils.Alignable;
import frc.robot.utils.AutoAlignment;
import frc.robot.utils.DriveMode;
import frc.robot.utils.loggingv2.LoggableCommand;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public class Drive extends LoggableCommand {
    private final SwerveDrivetrain drivetrain;

    private final DoubleSupplier fwdSupplier;
    private final DoubleSupplier strSupplier;
    private final DoubleSupplier rtSupplier;
    private boolean shouldFlip;
    private final Supplier<DriveMode> driveMode;

    public Drive(SwerveDrivetrain drivetrain, DoubleSupplier fwdSupplier, DoubleSupplier strSupplier, DoubleSupplier rtSupplier, Supplier<DriveMode> driveMode) {
        this.drivetrain = drivetrain;
        this.fwdSupplier = fwdSupplier;
        this.strSupplier = strSupplier;
        this.rtSupplier = rtSupplier;
        this.driveMode = driveMode;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        this.shouldFlip = RobotContainer.isRedAlliance();
    }

    @Override
    public void execute() {
        Alignable alignable = drivetrain.getAlignable();
        double fwd = MathUtil.applyDeadband(fwdSupplier.getAsDouble(),0.05)* Constants.MAX_VELOCITY;
        double str = MathUtil.applyDeadband(strSupplier.getAsDouble(), 0.05) * Constants.MAX_VELOCITY;
        ChassisSpeeds driveStates;
        if (alignable == null){
            double rcw = MathUtil.applyDeadband(rtSupplier.getAsDouble(), 0.05) * Constants.MAX_VELOCITY;
            drivetrain.setFacingTarget(false);
            driveStates = drivetrain.createChassisSpeeds(fwd * (shouldFlip ? 1 : -1), str * (shouldFlip ? 1 : -1), -rcw, driveMode.get());
        } else {
            double rcw = AutoAlignment.calcTurnSpeed(alignable, drivetrain.getPose(),drivetrain.getAlignableTurnPid()) * Constants.MAX_VELOCITY;
            drivetrain.setFacingTarget(AutoAlignment.angleFromTarget(alignable,drivetrain.getPose()) < Constants.AUTO_ALIGN_THRESHOLD);
            driveStates = drivetrain.createChassisSpeeds(fwd * (shouldFlip ? 1 : -1), str * (shouldFlip ? 1 : -1), rcw, driveMode.get());
        }
        drivetrain.drive(driveStates);
    }


    @Override
    public boolean isFinished() {
        return false;
    }
}