package frc.robot.commands;

import frc.robot.SwerveV2Drivetrain;
import frc.robot.utils.advanced.Alignable;
import frc.robot.utils.loggingv2.LoggableCommand;

public class SetAlignable extends LoggableCommand {
    private final Alignable alignable;
    private final SwerveV2Drivetrain drivetrain;

    public SetAlignable(SwerveV2Drivetrain drivetrain, Alignable alignable) {
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