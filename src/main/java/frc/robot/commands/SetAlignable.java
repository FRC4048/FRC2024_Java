package frc.robot.commands;

import frc.robot.subsystems.swervev3.SwerveDrivetrain;
import frc.robot.utils.Alignable;
import frc.robot.utils.loggingv2.LoggableCommand;

public class SetAlignable extends LoggableCommand {
    private final Alignable alignable;
    private final SwerveDrivetrain drivetrain;

    public SetAlignable(SwerveDrivetrain drivetrain, Alignable alignable) {
        this.drivetrain = drivetrain;
        this.alignable = alignable;
    }

    @Override
    public void initialize() {
        drivetrain.getAlignableTurnPid().reset();
        drivetrain.setAlignable(alignable);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
