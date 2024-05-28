package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.subsystems.swervev3.SwerveDrivetrain;
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
        DriverStation.reportWarning("SETTING ODOM",false);
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
