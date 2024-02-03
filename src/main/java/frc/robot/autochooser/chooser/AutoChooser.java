package frc.robot.autochooser.chooser;

import edu.wpi.first.math.geometry.Pose2d;
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
    public Pose2d getStartingPosition(){
        return provider.getSelectedLocation().getLocation();
    }

}
