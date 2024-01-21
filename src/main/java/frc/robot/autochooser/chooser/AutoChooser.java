package frc.robot.autochooser.chooser;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.event.AutoEventProvider;

public abstract class AutoChooser {
    public abstract Command getAutoCommand();
    private final AutoEventProvider provider;

    public AutoChooser(AutoEventProvider provider) {
        this.provider = provider;
    }

    public AutoEventProvider getProvider() {
        return provider;
    }
    public Translation2d getStartingPosition(){
        switch (provider.getSelectedLocation()){
            case Left -> {
                return provider.getSelectedAction().getLeftLocation();
            }
            case Middle -> {
                return provider.getSelectedAction().getMiddleLocation();
            }
            case Right -> {
                return provider.getSelectedAction().getRightLocation();
            }
            default -> {
                DriverStation.reportError("No auto action selected",true);
                return new Translation2d(0,0);
            }
        }
    }

}
