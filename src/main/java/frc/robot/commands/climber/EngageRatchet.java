package frc.robot.commands.climber;

import frc.robot.subsystems.climber.Climber;
import frc.robot.utils.loggingv2.LoggableCommand;

public class EngageRatchet extends LoggableCommand {

    private Climber climber;
    
    public EngageRatchet(Climber climber) {
        this.climber = climber;
    }

    @Override
    public void initialize() {
        climber.engageRatchet();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
