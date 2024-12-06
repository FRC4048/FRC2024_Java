package frc.robot.commands.drivetrain;

import frc.robot.SwerveV2Drivetrain;
import frc.robot.utils.DriveMode;
import frc.robot.utils.loggingv2.LoggableCommand;

public class SetRobotDriveMode extends LoggableCommand {
    private final SwerveV2Drivetrain drivetrain;
    private final DriveMode driveMode;

    public SetRobotDriveMode(SwerveV2Drivetrain drivetrain, DriveMode driveMode){
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