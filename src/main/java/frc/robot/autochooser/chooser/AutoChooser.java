package frc.robot.autochooser.chooser;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.autochooser.event.AutoEventProvider;

/**
 * base class for taking in a {@link AutoEvent} and returning the corresponding {@link Command}
 */
public abstract class AutoChooser {
    /**
     * @return Command that corresponds to the selected {@link AutoEvent}
     * from the {@link AutoEventProvider} specified by the method {@link #provider}
     */
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

    public void schedule() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'schedule'");
    }

}
