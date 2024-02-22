package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.Alignable;
import frc.robot.utils.AutoAlignment;

public class SetAlignable extends Command {
    private final Alignable alignable;
    private final SwerveDrivetrain drivetrain;

    public SetAlignable(SwerveDrivetrain drivetrain, Alignable alignable) {
        this.drivetrain = drivetrain;
        this.alignable = alignable;
    }

    @Override
    public void initialize() {
        AutoAlignment.resetPid();
        drivetrain.setAlignable(alignable);
    }
}
