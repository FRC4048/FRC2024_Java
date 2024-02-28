package frc.robot.commands.drivetrain;

import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.command.TimedSubsystemCommand;

public class ResetGyro extends TimedSubsystemCommand<SwerveDrivetrain> {
    public ResetGyro(SwerveDrivetrain drivetrain, int delay) {
        super(drivetrain, delay);
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        getSystem().resetGyro();
    }

    @Override
    public boolean runsWhenDisabled() {
        return true;
    }


}
