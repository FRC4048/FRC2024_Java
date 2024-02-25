package frc.robot.commands.drivetrain;

import frc.robot.autochooser.chooser.AutoChooser;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.command.SubsystemCommandBase;

public class SetInitOdom extends SubsystemCommandBase<SwerveDrivetrain> {
    private final AutoChooser chooser;

    public SetInitOdom(SwerveDrivetrain drivetrain, AutoChooser chooser) {
        super(drivetrain);
        this.chooser = chooser;
    }

    @Override
    public void initialize() {
        getSystem().setGyroOffset(chooser.getStartingPosition().getRotation().getDegrees() * -1);
        getSystem().resetOdometry(chooser.getStartingPosition());
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
