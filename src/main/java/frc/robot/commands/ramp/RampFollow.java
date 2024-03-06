package frc.robot.commands.ramp;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.constants.Constants;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.Alignable;
import frc.robot.utils.AutoAlignment;

import java.util.function.Supplier;

public class RampFollow extends Command {
    private final Ramp ramp;
    private final Supplier<Alignable> alignableSupplier;
    private Alignable alignable;
    private final Supplier<Pose2d> pose2dSupplier;

    public RampFollow(Ramp ramp, Supplier<Alignable> alignableSupplier, Supplier<Pose2d> pose2dSupplier) {
        this.ramp = ramp;
        this.alignableSupplier = alignableSupplier;
        this.pose2dSupplier = pose2dSupplier;
        addRequirements(ramp);
    }

    @Override
    public void initialize() {
        alignable = alignableSupplier.get();
    }

    @Override
    public void execute() {
        Pose2d pose = pose2dSupplier.get();
        if (alignable != null){
            Rotation2d targetAngle = new Rotation2d(Math.PI / 2) //complementarity angle
                    .minus(AutoAlignment.getYaw(alignable,
                            new Translation3d(pose.getX(),
                                    pose.getY(),
                                    Constants.HIGHT_OF_RAMP/2)
                            )
                    );
            if (Constants.RAMP_DEBUG) {
                SmartDashboard.putNumber("RAMP_TARGET_Angle",targetAngle.getDegrees());
            }
            ramp.setAngle(targetAngle);
        }
    }

    @Override
    public boolean isFinished() {
        boolean isFinished = alignable == null || alignableSupplier.get() == null || !alignable.equals(alignableSupplier.get());
        SmartDashboard.putBoolean("RampDone", isFinished);
        return isFinished;
    }
}
