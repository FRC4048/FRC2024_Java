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
            double encoderValue = Ramp.angleToEncoder(
                    new Rotation2d(Math.PI / 2)
                            .minus(AutoAlignment.getYaw(alignableNow,
                                    pose2dSupplier.get().getTranslation(),
                                    0.5)
                            ).getDegrees()
            );
            if (Constants.RAMP_DEBUG) SmartDashboard.putNumber("RAMP_TARGET_AUTO",encoderValue);
            ramp.setRampPos(encoderValue);
        }
    }

    @Override
    public boolean isFinished() {
        return alignableSupplier.get() == null;
    }
}
