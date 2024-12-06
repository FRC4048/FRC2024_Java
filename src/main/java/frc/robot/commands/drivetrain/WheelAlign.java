package frc.robot.commands.drivetrain;

import frc.robot.constants.Constants;
import frc.robot.SwerveV2Drivetrain;
import frc.robot.utils.loggingv2.LoggableCommand;

public class WheelAlign extends LoggableCommand {
    private final SwerveV2Drivetrain drivetrain;
    public WheelAlign(SwerveV2Drivetrain drivetrain){
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }
    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
    }

    /**
     * Has to be in init because execute was not getting called when command was in robot init for some reason
     */
    @Override
    public void initialize() {
        drivetrain.setSteerOffset(Constants.FRONT_LEFT_ABS_ENCODER_ZERO,Constants.FRONT_RIGHT_ABS_ENCODER_ZERO,Constants.BACK_LEFT_ABS_ENCODER_ZERO,Constants.BACK_RIGHT_ABS_ENCODER_ZERO);
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