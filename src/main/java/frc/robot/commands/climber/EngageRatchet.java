package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.climber.Climber;

public class EngageRatchet extends Command {

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
