package frc.robot.commands.climber;

import frc.robot.subsystems.Climber;
import frc.robot.utils.command.SubsystemCommandBase;

public class EngageRatchet extends SubsystemCommandBase<Climber> {

    public EngageRatchet(Climber climber) {
        super(climber);
    }

    @Override
    public void initialize() {
        getSystem().engageRatchet();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
