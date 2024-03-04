package frc.robot.commands.ramp;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
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
    private final Supplier<Pose2d> pose2dSupplier;

    public RampFollow(Ramp ramp, Supplier<Alignable> alignableSupplier, Supplier<Pose2d> pose2dSupplier) {
        this.ramp = ramp;
        this.alignableSupplier = alignableSupplier;
        this.pose2dSupplier = pose2dSupplier;
        addRequirements(ramp);
    }

    @Override
    public void execute() {
        Alignable alignableNow = alignableSupplier.get();
        if (alignableNow != null){
            Rotation2d targetAngle = new Rotation2d(Math.PI / 2) //complementarity angle
                    .minus(AutoAlignment.getYaw(alignableNow,
                            pose2dSupplier.get().getTranslation(),
                            Constants.HIGHT_OF_RAMP)
                    );
            if (Constants.RAMP_DEBUG) {
                SmartDashboard.putNumber("RAMP_TARGET_Angle",targetAngle.getDegrees());
            }
            ramp.setAngle(targetAngle);
        }
    }

    @Override
    public boolean isFinished() {
        return alignableSupplier.get() == null;
    }
}
