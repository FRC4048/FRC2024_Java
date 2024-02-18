package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;

public class ReportErrorCommand extends Command {

    @Override
    public void execute() {
        DriverStation.reportError("Purposeful error",false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
