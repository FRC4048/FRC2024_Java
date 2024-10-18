package frc.robot.commands.climber;

import frc.robot.subsystems.climber.Climber;
import frc.robot.utils.loggingv2.LoggableCommand;

public class DisengageRatchet extends LoggableCommand {

    private Climber climber;
    
    public DisengageRatchet(Climber climber) {
        this.climber = climber;
    }

    @Override
    public void initialize() {
        climber.disengageRatchet();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
