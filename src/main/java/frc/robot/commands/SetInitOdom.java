package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.subsystems.swervev2.SwerveDrivetrain;

public class SetInitOdom extends Command {
    private final SwerveDrivetrain drivetrain;
    private final AutoChooser chooser;

    public SetInitOdom(SwerveDrivetrain drivetrain, AutoChooser chooser) {
        this.drivetrain = drivetrain;
        this.chooser = chooser;
    }

    @Override
    public void initialize() {
        drivetrain.setGyroOffset(chooser.getStartingPosition().getRotation().getDegrees()*-1);
        drivetrain.resetOdometry(chooser.getStartingPosition());
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }
}
