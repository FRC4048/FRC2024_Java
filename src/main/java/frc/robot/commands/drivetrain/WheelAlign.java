package frc.robot.commands.drivetrain;

import frc.robot.constants.Constants;
import frc.robot.subsystems.swervev3.SwerveDrivetrain;
import frc.robot.utils.loggingv2.LoggableCommand;

public class WheelAlign extends LoggableCommand {
    private final SwerveDrivetrain drivetrain;
    public WheelAlign(SwerveDrivetrain drivetrain){
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

    @Override
    public void execute() {
        drivetrain.setSteerOffset(Constants.FRONT_LEFT_ABS_ENCODER_ZERO,Constants.FRONT_RIGHT_ABS_ENCODER_ZERO,Constants.BACK_LEFT_ABS_ENCODER_ZERO,Constants.BACK_RIGHT_ABS_ENCODER_ZERO);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public boolean runsWhenDisabled() {
        return false;
    }
}
