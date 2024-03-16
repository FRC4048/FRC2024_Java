package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveDrivetrain;
import frc.robot.utils.DriveMode;

public class ToggleDrivingMode extends Command {
    private final SwerveDrivetrain drivetrain;

    public ToggleDrivingMode(SwerveDrivetrain drivetrain){
        this.drivetrain = drivetrain;
    }

    @Override
    public void execute() {
        switch (drivetrain.getDriveMode()){
            case FIELD_CENTRIC -> drivetrain.setDriveMode(DriveMode.ROBOT_CENTRIC);
            case ROBOT_CENTRIC -> drivetrain.setDriveMode(DriveMode.FIELD_CENTRIC);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
