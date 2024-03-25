package frc.robot.commands.ramp;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;
import frc.robot.utils.Alignable;

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
        double v = ramp.calcPose(pose2dSupplier.get(), alignable);
        SmartDashboard.putNumber("v", v);
        ramp.setRampPos(v);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
