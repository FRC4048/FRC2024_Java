package frc.robot.commands.ramp;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Ramp;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.Alignable;

public class BoringRampFollow extends Command {
    private final Ramp ramp;
    private final SwerveDrivetrain drivetrain;
    private Alignable alignable;

    public BoringRampFollow(Ramp ramp, SwerveDrivetrain drivetrain) {
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
        Alignable now = drivetrain.getAlignable();
        if (now != null){
            double v = ramp.calcPose(drivetrain.getPose(), alignable);
            ramp.setRampPos(v);
        }

    }

    @Override
    public boolean isFinished() {
        return alignable == null || drivetrain.getAlignable() == null || !alignable.equals(drivetrain.getAlignable());
    }
}
