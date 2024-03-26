package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.DriveMode;

public class SetRobotDriveMode extends Command {
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
