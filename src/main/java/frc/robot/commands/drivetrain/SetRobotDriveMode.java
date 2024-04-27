package frc.robot.commands.drivetrain;

import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.DriveMode;
import frc.robot.utils.loggingv2.LoggableCommand;

public class SetRobotDriveMode extends LoggableCommand {
    private final SwerveDrivetrain drivetrain;
    private final DriveMode driveMode;

    public SetRobotDriveMode(SwerveDrivetrain drivetrain, DriveMode driveMode){
        this.drivetrain = drivetrain;
        this.driveMode = driveMode;
    }

    @Override
    public void execute() {
        drivetrain.setDriveMode(driveMode);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
