package frc.robot.commands.drivetrain;

import frc.robot.constants.Constants;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.command.SubsystemCommandBase;

public class WheelAlign extends SubsystemCommandBase<SwerveDrivetrain> {
    public WheelAlign(SwerveDrivetrain drivetrain){
        super(drivetrain);
    }

    /**
     * Has to be in init because execute was not getting called when command was in robot init for some reason
     */
    @Override
    public void initialize() {
        getSystem().setSteerOffset(Constants.FRONT_LEFT_ABS_ENCODER_ZERO,Constants.FRONT_RIGHT_ABS_ENCODER_ZERO,Constants.BACK_LEFT_ABS_ENCODER_ZERO,Constants.BACK_RIGHT_ABS_ENCODER_ZERO);
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
