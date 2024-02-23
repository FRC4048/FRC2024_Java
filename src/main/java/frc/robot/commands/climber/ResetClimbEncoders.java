package frc.robot.commands.climber;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Climber;

public class ResetClimbEncoders extends Command {

    private Climber climber;
    
    public ResetClimbEncoders(Climber climber) {
        this.climber = climber;
    }

    @Override
    public void initialize() {
        climber.resetEncoders();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    
}
