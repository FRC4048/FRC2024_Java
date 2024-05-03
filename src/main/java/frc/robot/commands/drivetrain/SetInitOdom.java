package frc.robot.commands.drivetrain;

import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.swervev3.SwerveDrivetrain;
import frc.robot.utils.loggingv2.LoggableCommand;

public class SetInitOdom extends LoggableCommand {
    private final SwerveDrivetrain drivetrain;
    private final AutoChooser chooser;

    public SetInitOdom(SwerveDrivetrain drivetrain, AutoChooser chooser) {
        this.drivetrain = drivetrain;
        this.chooser = chooser;
    }

    @Override
    public void initialize() {
        drivetrain.setGyroOffset(chooser.getStartingPosition().getRotation().getDegrees() * -1);
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
