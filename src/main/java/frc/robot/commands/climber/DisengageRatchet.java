package frc.robot.commands.climber;

import frc.robot.subsystems.Climber;
import frc.robot.utils.command.SubsystemCommandBase;

public class DisengageRatchet extends SubsystemCommandBase<Climber> {

    public DisengageRatchet(Climber climber) {
        super(climber);
    }

    @Override
    public void initialize() {
        getSystem().disengageRatchet();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
