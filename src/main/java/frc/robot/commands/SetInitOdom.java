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
//        double newOffset = rangeConvert(-180, 180, -360, 360, chooser.getStartingPosition().getRotation().getDegrees());
        drivetrain.setGyroOffset(chooser.getStartingPosition().getRotation().getDegrees()*-1);
        drivetrain.resetOdometry(chooser.getStartingPosition());
    }
    private static double rangeConvert(double startMin, double startMax, double endMin, double endMax, double value){
        double oldRange = startMax - startMin;
        double newRange = endMax - endMin;
        return (((value - startMin) * newRange) / oldRange) + endMin;
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
