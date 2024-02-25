package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class DisengageRatchet extends Command {

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
