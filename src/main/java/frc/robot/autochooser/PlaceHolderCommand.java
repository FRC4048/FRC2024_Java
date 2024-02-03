package frc.robot.autochooser;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.*;

//Why is CommandBase abstract if it has not abstract methods????
public class PlaceHolderCommand extends Command{
    @Override
    public void execute() {
        DriverStation.reportError("TEST",false);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
